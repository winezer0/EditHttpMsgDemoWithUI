package burp;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Map;

import static burp.Config.loadConfigVarsToMap;
import static burp.Config.updateConfigVarsFromMap;


/**
 * 工具模块监听配置面板
 * 提供复选框界面用于选择需要监听的 Burp Suite 工具模块
 */
public class ToolPanel extends JPanel {
    private JCheckBox proxyCheckBox;
    private JCheckBox repeaterCheckBox;
    private JCheckBox scannerCheckBox;
    private JCheckBox intruderCheckBox;
    private JCheckBox extenderCheckBox;

    // 名单输入框
    private JTextField whiteHostField;
    private JTextField blackHostField;
    private JTextField whiteMethodField;
    private JTextField blackMethodField;
    private JTextField whitePathField;
    private JTextField blackPathField;
    private JTextField blackSuffixField;
    private JTextField whiteBodykeysField;

    // 配置存储和加载按钮
    private JButton updateConfigButton;
    private JButton showConfigButton;
    private JButton storeConfigButton;
    private JButton loadConfigButton;
    private JButton exportConfigButton;
    private JButton importConfigButton;

    /**
     * 构造函数，初始化面板
     */
    public ToolPanel() {
        initPanel();
    }

    /**
     * 初始化面板布局和组件
     */
    private void initPanel() {
        // 设置面板布局
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // 创建主面板
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // 创建工具选择部分
        JPanel toolSection = createToolSection();
        mainPanel.add(toolSection);
        mainPanel.add(Box.createVerticalStrut(15));

        // 创建名单配置部分
        JPanel listSection = createListSection();
        mainPanel.add(listSection);
        mainPanel.add(Box.createVerticalStrut(15));

        // 创建配置管理部分
        JPanel configSection = createConfigSection();
        mainPanel.add(configSection);

        // 添加滚动面板
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * 创建工具选择部分
     */
    private JPanel createToolSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // 标题
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("Select tools to listen for HTTP traffic:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titlePanel.add(titleLabel);
        panel.add(titlePanel);

        // 复选框面板 - 改为一行显示
        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 5));
        checkBoxPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        // 初始化复选框
        proxyCheckBox = createToolCheckBox("Proxy", Config.LISTEN_PROXY);
        repeaterCheckBox = createToolCheckBox("Repeater", Config.LISTEN_REPEATER);
        scannerCheckBox = createToolCheckBox("Scanner", Config.LISTEN_SCANNER);
        intruderCheckBox = createToolCheckBox("Intruder", Config.LISTEN_INTRUDER);
        extenderCheckBox = createToolCheckBox("Extender", Config.LISTEN_EXTENDER);

        // 添加复选框到面板
        checkBoxPanel.add(proxyCheckBox);
        checkBoxPanel.add(repeaterCheckBox);
        checkBoxPanel.add(scannerCheckBox);
        checkBoxPanel.add(intruderCheckBox);
        checkBoxPanel.add(extenderCheckBox);

        panel.add(checkBoxPanel);
        return panel;
    }

    /**
     * 创建名单配置部分
     */
    private JPanel createListSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // 标题
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("List Config (empty means no matching, please press Enter or click Store Config):");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titlePanel.add(titleLabel);
        panel.add(titlePanel);

        // 输入框面板
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(new EmptyBorder(10, 20, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 0, 5, 10);

        // 使用通用方法创建所有输入框
        whiteHostField = createLabeledTextField(inputPanel, gbc,"White Host RegEx:", Config.WHITE_HOST_REGX,   value -> Config.WHITE_HOST_REGX = value);
        blackHostField = createLabeledTextField(inputPanel, gbc,"Black Host RegEx:", Config.BLACK_HOST_REGX,value -> Config.BLACK_HOST_REGX = value);
        whiteMethodField = createLabeledTextField(inputPanel, gbc,"White Method RegEx:", Config.WHITE_METHOD_REGX,value -> Config.WHITE_METHOD_REGX = value);
        blackMethodField = createLabeledTextField(inputPanel, gbc,"Black Method RegEx:", Config.BLACK_METHOD_REGX,value -> Config.BLACK_METHOD_REGX = value);
        whitePathField = createLabeledTextField(inputPanel, gbc,"White Path RegEx:", Config.WHITE_PATH_REGX,value -> Config.WHITE_PATH_REGX = value);
        blackPathField = createLabeledTextField(inputPanel, gbc,"Black Path RegEx:", Config.BLACK_PATH_REGX,value -> Config.BLACK_PATH_REGX = value);
        blackSuffixField = createLabeledTextField(inputPanel, gbc,"Black Suffix RegEx:", Config.BLACK_SUFFIX_REGX,  value -> Config.BLACK_SUFFIX_REGX = value);
        whiteBodykeysField = createLabeledTextField(inputPanel, gbc, "White Body RegEx:", Config.WHITE_BODY_REGX,  value -> Config.WHITE_BODY_REGX = value);

        panel.add(inputPanel);
        return panel;
    }

    /**
     * 创建配置管理部分
     */
    private JPanel createConfigSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // 标题
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("Config Management:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titlePanel.add(titleLabel);
        panel.add(titlePanel);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBorder(new EmptyBorder(10, 20, 10, 10));

        updateConfigButton = new JButton("Update Config");
        updateConfigButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateConfigVarsFromUiText();
                if (BurpExtender.stdout != null) {
                    BurpExtender.stdout.println("[Config] UI Config updated to ConfigVars");
                }
                showMessage("Success", "Config updated from UI");
            }
        });

        showConfigButton = new JButton("Show Config");
        showConfigButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showConfigDialog();
            }
        });

        storeConfigButton = new JButton("Store Config");
        storeConfigButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                storeConfig();
            }
        });

        loadConfigButton = new JButton("Load Config");
        loadConfigButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadConfig();
            }
        });

        exportConfigButton = new JButton("Export Config");
        exportConfigButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportConfig();
            }
        });

        importConfigButton = new JButton("Import Config");
        importConfigButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importConfig();
            }
        });

        buttonPanel.add(updateConfigButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(showConfigButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(storeConfigButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(loadConfigButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(exportConfigButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(importConfigButton);

        panel.add(buttonPanel);
        return panel;
    }

    /**
     * 创建带标签的输入框
     * @param panel 目标面板
     * @param gbc 布局约束
     * @param labelText 标签文本
     * @param initialValue 初始值
     * @param updateAction 更新配置的回调函数
     * @return 创建好的输入框组件
     */
    private JTextField createLabeledTextField(JPanel panel, GridBagConstraints gbc,
            String labelText, String initialValue, java.util.function.Consumer<String> updateAction) {
        // 添加标签
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panel.add(new JLabel(labelText), gbc);

        // 创建输入框
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JTextField textField = new JTextField(initialValue, 30);
        textField.addActionListener(e -> updateAction.accept(textField.getText()));
        panel.add(textField, gbc);

        // 移动到下一行
        gbc.gridy++;

        return textField;
    }

    /**
     * 创建工具模块复选框
     * @param toolName 工具名称
     * @param initialState 初始选中状态
     * @return 配置好的复选框组件
     */
    private JCheckBox createToolCheckBox(String toolName, boolean initialState) {
        JCheckBox checkBox = new JCheckBox(toolName);
        checkBox.setSelected(initialState);
        checkBox.setFont(new Font("Arial", Font.PLAIN, 13));

        // 添加状态变更监听器
        checkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateConfig(toolName, checkBox.isSelected());
            }
        });

        return checkBox;
    }

    /**
     * 更新配置类中的监听状态
     * @param toolName 工具名称
     * @param isSelected 是否选中
     */
    private void updateConfig(String toolName, boolean isSelected) {
        switch (toolName) {
            case "Proxy":
                Config.LISTEN_PROXY = isSelected;
                break;
            case "Repeater":
                Config.LISTEN_REPEATER = isSelected;
                break;
            case "Scanner":
                Config.LISTEN_SCANNER = isSelected;
                break;
            case "Intruder":
                Config.LISTEN_INTRUDER = isSelected;
                break;
            case "Extender":
                Config.LISTEN_EXTENDER = isSelected;
                break;
        }

        if (BurpExtender.stdout != null) {
            BurpExtender.stdout.println(String.format("[Config] %s module listening: %s", toolName, isSelected));
        }
    }

    /**
     * 存储配置到 Burp 项目
     */
    public void storeConfig() {
        if (BurpExtender.callbacks == null) {
            showMessage("Error", "Burp Extender callbacks not initialized");
            return;
        }
        // 保存当前配置到输入框
        updateConfigVarsFromUiText();
        // 序列化并存储
        try {
            // 构建配置映射
            Map<String, Object> configMap = loadConfigVarsToMap();
            ObjectMapper objectMapper = new ObjectMapper();
            String configJson = objectMapper.writeValueAsString(configMap);
            BurpExtender.callbacks.saveExtensionSetting(BurpExtender.ExtenderName, configJson);
            if (BurpExtender.stdout != null) {
                BurpExtender.stdout.println("[Config] Config stored successfully");
            }
            showMessage("Success", "Config stored successfully");
        } catch (Exception e) {
            if (BurpExtender.stderr != null) {
                BurpExtender.stderr.println("[Error] Failed to store config: " + e.getMessage());
            }
            showMessage("Error", "Failed to store config: " + e.getMessage());
        }
    }

    /**
     * 从 Burp 项目加载配置
     */
    public void loadConfig() {
        if (BurpExtender.callbacks == null) {
            showMessage("Error", "Burp Extender callbacks not initialized");
            return;
        }

        try {
            String configJson = BurpExtender.callbacks.loadExtensionSetting(BurpExtender.ExtenderName);
            if (configJson != null && !configJson.isEmpty()) {
                // 反序列化配置
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> configMap = objectMapper.readValue(configJson, Map.class);
                // 加载配置
                updateConfigVarsFromMap(configMap);
                // 更新 UI
                updateUiTextFromConfigVars();

                if (BurpExtender.stdout != null) {
                    BurpExtender.stdout.println("[Config] Config loaded successfully");
                }
                showMessage("Success", "Config loaded successfully");
            } else {
                showMessage("Info", "No saved Config found");
            }
        } catch (Exception e) {
            if (BurpExtender.stderr != null) {
                BurpExtender.stderr.println("[Error] Failed to load config: " + e.getMessage());
            }
            showMessage("Error", "Failed to load config: " + e.getMessage());
        }
    }

    // 加载当前的Config配置到UI输入框
    private void updateUiTextFromConfigVars() {
        whiteHostField.setText(Config.WHITE_HOST_REGX);
        blackHostField.setText(Config.BLACK_HOST_REGX);
        whiteMethodField.setText(Config.WHITE_METHOD_REGX);
        blackMethodField.setText(Config.BLACK_METHOD_REGX);
        whitePathField.setText(Config.WHITE_PATH_REGX);
        blackPathField.setText(Config.BLACK_PATH_REGX);
        blackSuffixField.setText(Config.BLACK_SUFFIX_REGX);
        whiteBodykeysField.setText(Config.WHITE_BODY_REGX);

        proxyCheckBox.setSelected(Config.LISTEN_PROXY);
        repeaterCheckBox.setSelected(Config.LISTEN_REPEATER);
        scannerCheckBox.setSelected(Config.LISTEN_SCANNER);
        intruderCheckBox.setSelected(Config.LISTEN_INTRUDER);
        extenderCheckBox.setSelected(Config.LISTEN_EXTENDER);
    }

    // 加载当前的UI输入框内容到Config配置
    private void updateConfigVarsFromUiText() {
        // 保存当前配置到输入框
        Config.WHITE_HOST_REGX = whiteHostField.getText();
        Config.BLACK_HOST_REGX = blackHostField.getText();
        Config.WHITE_METHOD_REGX = whiteMethodField.getText();
        Config.BLACK_METHOD_REGX = blackMethodField.getText();
        Config.WHITE_PATH_REGX = whitePathField.getText();
        Config.BLACK_PATH_REGX = blackPathField.getText();
        Config.BLACK_SUFFIX_REGX = blackSuffixField.getText();
        Config.WHITE_BODY_REGX = whiteBodykeysField.getText();

        // 保存复选框状态到Config
        Config.LISTEN_PROXY = proxyCheckBox.isSelected();
        Config.LISTEN_REPEATER = repeaterCheckBox.isSelected();
        Config.LISTEN_SCANNER = scannerCheckBox.isSelected();
        Config.LISTEN_INTRUDER = intruderCheckBox.isSelected();
        Config.LISTEN_EXTENDER = extenderCheckBox.isSelected();
    }

    /**
     * 显示当前配置对话框
     */
    private void showConfigDialog() {
        // 先更新配置
        updateConfigVarsFromUiText();
        // 获取配置映射
        Map<String, Object> configMap = loadConfigVarsToMap();
        // 构建配置文本
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : configMap.entrySet()) {
            sb.append(entry.getKey())
              .append(": ")
              .append(entry.getValue())
              .append("\n");
        }
        
        // 创建文本区域显示配置
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        // 显示对话框
        JOptionPane.showMessageDialog(this, scrollPane, "Current Config", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 导出配置到 JSON 文件
     */
    public void exportConfig() {
        updateConfigVarsFromUiText();
        // 构建配置映射
        Map<String, Object> configMap = loadConfigVarsToMap();
        // 打开文件选择器
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Config");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("JSON Files", "json"));
        fileChooser.setSelectedFile(new File(BurpExtender.ExtenderName + ".json"));

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().endsWith(".json")) {
                file = new File(file.getPath() + ".json");
            }

            // 序列化并保存到文件
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, configMap);
                if (BurpExtender.stdout != null) {
                    BurpExtender.stdout.println("[Config] Config exported successfully to: " + file.getAbsolutePath());
                }
                showMessage("Success", "Config exported successfully to:\n" + file.getAbsolutePath());
            } catch (Exception e) {
                if (BurpExtender.stderr != null) {
                    BurpExtender.stderr.println("[Error] Failed to export config: " + e.getMessage());
                }
                showMessage("Error", "Failed to export config: " + e.getMessage());
            }
        }
    }


    /**
     * 从 JSON 文件导入配置
     */
    public void importConfig() {
        // 打开文件选择器
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Import Config");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("JSON Files", "json"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try {
                // 从文件读取并反序列化配置
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> configMap = objectMapper.readValue(file, Map.class);
                // 更新配置
                updateConfigVarsFromMap(configMap);
                // 更新 UI
                updateUiTextFromConfigVars();

                if (BurpExtender.stdout != null) {
                    BurpExtender.stdout.println("[Config] Config imported successfully from: " + file.getAbsolutePath());
                }
                showMessage("Success", "Config imported successfully from:\n" + file.getAbsolutePath());
            } catch (Exception e) {
                if (BurpExtender.stderr != null) {
                    BurpExtender.stderr.println("[Error] Failed to import config: " + e.getMessage());
                }
                showMessage("Error", "Failed to import config: " + e.getMessage());
            }
        }
    }

    /**
     * 显示消息框
     * @param title 标题
     * @param message 消息内容
     */
    private void showMessage(String title, String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
        });
    }
}
