package burp;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.util.List;

/**
 * Burp Suite 扩展主类
 * 实现 IBurpExtender、IHttpListener 和 ITab 接口
 * 用于拦截和修改 HTTP 请求消息
 */
public class BurpExtender implements IBurpExtender, IHttpListener, ITab {
    public static BurpExtender burpExtender;
    public static IBurpExtenderCallbacks callbacks;
    public static PrintWriter stdout;
    public static PrintWriter stderr;

    public static String ExtenderName;
    public static String ExtenderVersion;

    private IExtensionHelpers helpers;
    private ToolPanel toolPanel;
    private JPanel mainPanel;

    /**
     * 注册扩展回调
     * @param callbacks Burp 扩展回调接口
     */
    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
        this.callbacks = callbacks;
        this.helpers = callbacks.getHelpers();
        burpExtender = this;
        stdout = new PrintWriter(callbacks.getStdout(), true);
        stderr = new PrintWriter(callbacks.getStderr(), true);
        ExtenderName = Config.EXTENSION_NAME;
        ExtenderVersion = Config.EXTENSION_VERSION;
        callbacks.setExtensionName(ExtenderName + "_" + ExtenderVersion);
        callbacks.registerHttpListener(this);

        // 初始化并注册 UI
        SwingUtilities.invokeLater(() -> {
            initUI();
            callbacks.addSuiteTab(BurpExtender.this);
        });

        stdout.println(String.format("[*] %s %s loaded successfully", ExtenderName, ExtenderVersion));
    }

    /**
     * 初始化 UI 面板
     */
    private void initUI() {
        mainPanel = new JPanel(new BorderLayout());
        toolPanel = new ToolPanel();
        mainPanel.add(toolPanel, BorderLayout.NORTH);
    }

    /**
     * 获取 Tab 标题
     * @return Tab 显示名称
     */
    @Override
    public String getTabCaption() {
        return ExtenderName;
    }

    /**
     * 获取 UI 组件
     * @return 主面板组件
     */
    @Override
    public Component getUiComponent() {
        return mainPanel;
    }

    /**
     * 处理 HTTP 消息
     * @param toolFlag 工具标识
     * @param messageIsRequest 是否为请求消息
     * @param messageInfo HTTP 请求响应信息
     */
    @Override
    public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo) {
        // 检查是否需要处理该工具的流量
        if (!shouldProcessTool(toolFlag)) {
            stdout.println("is not should process tool:" +  toolFlag);
            return;
        }

        // 修改数据并进行发送
        if (messageIsRequest) {
            processRequest(messageInfo);
        }
    }

    /**
     * 检查是否应该处理指定工具的流量
     * @param toolFlag 工具标识
     * @return 是否需要处理
     */
    private boolean shouldProcessTool(int toolFlag) {
        switch (toolFlag) {
            case IBurpExtenderCallbacks.TOOL_PROXY:
                return Config.LISTEN_PROXY;
            case IBurpExtenderCallbacks.TOOL_REPEATER:
                return Config.LISTEN_REPEATER;
            case IBurpExtenderCallbacks.TOOL_SCANNER:
                return Config.LISTEN_SCANNER;
            case IBurpExtenderCallbacks.TOOL_INTRUDER:
                return Config.LISTEN_INTRUDER;
            case IBurpExtenderCallbacks.TOOL_EXTENDER:
                return Config.LISTEN_EXTENDER;
            default:
                return false;
        }
    }

    /**
     * 处理 HTTP 请求
     * @param messageInfo HTTP 请求响应信息
     */
    private void processRequest(IHttpRequestResponse messageInfo) {
        IRequestInfo analyzeRequest = helpers.analyzeRequest(messageInfo);

        // 获取 HOST
        String reqHost = messageInfo.getHttpService().getHost();
        // 白名单 HOST 匹配
        if (!MatchUtils.isMatchKeywords(Config.WHITE_HOST_REGX, reqHost, true)) {
            return;
        }

        // 黑名单 HOST 匹配
        if (MatchUtils.isMatchKeywords(Config.BLACK_HOST_REGX, reqHost, false)) {
            return;
        }

        // 获取请求方法
        String reqMethod = analyzeRequest.getMethod();
        // 白名单方法匹配
        if (!MatchUtils.isMatchKeywords(Config.WHITE_METHOD_REGX, reqMethod, true)) {
            return;
        }

        // 黑名单方法匹配
        if (MatchUtils.isMatchKeywords(Config.BLACK_METHOD_REGX, reqMethod, false)) {
            return;
        }

        // 获取请求路径
        String reqPath = analyzeRequest.getUrl().getPath();

        // 白名单 PATH 匹配
        if (!MatchUtils.isMatchKeywords(Config.WHITE_PATH_REGX, reqHost, true)) {
            return;
        }

        // 黑名单路径匹配
        if (MatchUtils.isMatchKeywords(Config.BLACK_PATH_REGX, reqPath, false)) {
            return;
        }

        // 黑名单后缀匹配
        if (MatchUtils.isMatchBlackSuffix(Config.BLACK_SUFFIX_REGX, reqPath, false)) {
            return;
        }

        // 获取请求体
        String body = "";
        if (MatchUtils.shouldBeHasBody(reqMethod)) {
            // 获取 body
            int bodyOffset = analyzeRequest.getBodyOffset();
            byte[] byteRequest = messageInfo.getRequest();
            String request = new String(byteRequest);
            body = request.substring(bodyOffset);
        }

        // 检查请求体关键字
        if (!MatchUtils.isMatchKeywords(Config.WHITE_BODY_REGX, body, true)) {
            return;
        }

        // 获取 header 列表
        List<String> reqHeaders = analyzeRequest.getHeaders();
        // 修改请求体数据
        String newBody = ProcessAction.updateBodyData(body);
        stdout.println(String.format("old body length: [%s] -> new body length: [%s]", body.length(), newBody.length()));
        // 构建新的请求
        byte[] newRequest = helpers.buildHttpMessage(reqHeaders, newBody.getBytes());
        messageInfo.setRequest(newRequest);
    }
}
