package com.fieldnation.wsb;

import javax.swing.JFrame;
import javax.swing.UIManager;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.FlowLayout;
import javax.swing.JScrollPane;

public abstract class WsbUi extends JFrame {
	private static final long serialVersionUID = 1L;
	public JTextField urlTextField;
	public JTextField usernameTextField;
	public JPasswordField passwordTextField;
	public JComboBox methodComboBox;
	public JCheckBox moreChkBox;
	public JComboBox mimeContentType;
	public JTextArea postDataTextArea;
	public JTextArea logTextArea;
	private JButton goButton;
	private JTabbedPane tabbedPane;
	private JPanel morePanel;
	private JPanel postDataPanel;
	private JPanel logPanel;
	private JLabel mimeLabel;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	private JButton btnClear;

	public WsbUi() {
		setTitle("Webservice Browser");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
		}

		this.setLocationRelativeTo(null);
		this.setSize(648, 480);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 1.0 };
		gridBagLayout.columnWeights = new double[] { 1.0 };
		getContentPane().setLayout(gridBagLayout);

		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.weightx = 1.0;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		getContentPane().add(panel, gbc_panel);

		methodComboBox = new JComboBox();
		methodComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String method = ((String) methodComboBox.getSelectedItem()).toString().toLowerCase();
				if ("post".equals(method) || "put".equals(method)) {
					mimeLabel.setVisible(true);
					mimeContentType.setVisible(true);
				} else {
					mimeLabel.setVisible(false);
					mimeContentType.setVisible(false);
				}
			}
		});
		methodComboBox.setModel(new DefaultComboBoxModel(new String[] { "GET", "POST", "PUT", "DELETE" }));
		panel.add(methodComboBox);

		urlTextField = new JTextField();
		urlTextField.setText("http://");
		panel.add(urlTextField);
		urlTextField.setColumns(47);

		goButton = new JButton("Go");
		getRootPane().setDefaultButton(goButton);
		goButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedComponent(logPanel);

				goButton_onClick(e);
			}
		});
		panel.add(goButton);

		moreChkBox = new JCheckBox("More");
		moreChkBox.setSelected(true);
		moreChkBox.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (moreChkBox.isSelected()) {
					morePanel.setVisible(true);
				} else {
					morePanel.setVisible(false);
				}
			}
		});
		panel.add(moreChkBox);

		morePanel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) morePanel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		GridBagConstraints gbc_morePanel = new GridBagConstraints();
		gbc_morePanel.insets = new Insets(0, 0, 5, 0);
		gbc_morePanel.weightx = 1.0;
		gbc_morePanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_morePanel.gridx = 0;
		gbc_morePanel.gridy = 1;
		getContentPane().add(morePanel, gbc_morePanel);

		JLabel lblNewLabel = new JLabel("Username");
		morePanel.add(lblNewLabel);

		usernameTextField = new JTextField();
		morePanel.add(usernameTextField);
		usernameTextField.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Password");
		morePanel.add(lblNewLabel_1);

		passwordTextField = new JPasswordField();
		morePanel.add(passwordTextField);
		passwordTextField.setColumns(10);

		mimeLabel = new JLabel("MIME");
		mimeLabel.setVisible(false);
		morePanel.add(mimeLabel);

		mimeContentType = new JComboBox();
		mimeContentType.setVisible(false);
		mimeContentType.setModel(new DefaultComboBoxModel(new String[] { "application/json", "application/x-www-form-urlencoded", "application/xml", "text/xml" }));
		mimeContentType.setEditable(true);
		morePanel.add(mimeContentType);

		btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				logTextArea.setText("");
			}
		});
		GridBagConstraints gbc_btnClear = new GridBagConstraints();
		gbc_btnClear.anchor = GridBagConstraints.NORTHEAST;
		gbc_btnClear.gridx = 0;
		gbc_btnClear.gridy = 2;
		getContentPane().add(btnClear, gbc_btnClear);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 2;
		getContentPane().add(tabbedPane, gbc_tabbedPane);

		postDataPanel = new JPanel();
		tabbedPane.addTab("POST Data", null, postDataPanel, null);
		GridBagLayout gbl_postDataPanel = new GridBagLayout();
		postDataPanel.setLayout(gbl_postDataPanel);

		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.weighty = 1.0;
		gbc_scrollPane.weightx = 1.0;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		postDataPanel.add(scrollPane, gbc_scrollPane);

		postDataTextArea = new JTextArea();
		scrollPane.setViewportView(postDataTextArea);

		logPanel = new JPanel();
		tabbedPane.addTab("Log", null, logPanel, null);
		GridBagLayout gbl_logPanel = new GridBagLayout();
		logPanel.setLayout(gbl_logPanel);

		scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.weighty = 1.0;
		gbc_scrollPane_1.weightx = 1.0;
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 0;
		logPanel.add(scrollPane_1, gbc_scrollPane_1);

		logTextArea = new JTextArea();
		scrollPane_1.setViewportView(logTextArea);
	}

	public abstract void goButton_onClick(ActionEvent e);
}
