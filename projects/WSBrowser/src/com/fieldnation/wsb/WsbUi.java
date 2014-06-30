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

import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;

public abstract class WsbUi extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField urlTextField;
	private JTextField usernameTextField;
	private JPasswordField passwordTextField;
	private JComboBox methodComboBox;
	private JCheckBox moreChkBox;
	private JComboBox mimeContentType;
	private JTextArea postDataTextArea;
	private JTextArea logTextArea;
	private JButton goButton;
	private JTabbedPane tabbedPane;
	private JPanel morePanel;
	private JPanel postDataPanel;
	private JPanel logPanel;
	private JLabel mimeLabel;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	private JButton btnClear;
	private JPanel j2jPanel;
	private JTextField j2jPackageTextField;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;
	private JTextField j2jOutTextField;
	private JCheckBox j2jChkBox;

	public WsbUi() {
		setTitle("Webservice Browser");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
		}

		this.setLocationRelativeTo(null);
		this.setSize(659, 480);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0 };
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
		methodComboBox.setModel(new DefaultComboBoxModel(
				new String[] { "GET", "POST", "PUT", "DELETE" }));
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

		j2jChkBox = new JCheckBox("J2J");
		panel.add(j2jChkBox);
		j2jChkBox.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if (j2jChkBox.isSelected()) {
					j2jPanel.setVisible(true);
				} else {
					j2jPanel.setVisible(false);
				}
			}
		});

		morePanel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) morePanel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		GridBagConstraints gbc_morePanel = new GridBagConstraints();
		gbc_morePanel.insets = new Insets(0, 0, 5, 0);
		gbc_morePanel.weightx = 1.0;
		gbc_morePanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_morePanel.gridx = 0;
		gbc_morePanel.gridy = 2;
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
		mimeContentType.setModel(new DefaultComboBoxModel(
				new String[] { "application/json", "application/x-www-form-urlencoded", "application/xml", "text/xml" }));
		mimeContentType.setEditable(true);
		morePanel.add(mimeContentType);

		btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				logTextArea.setText("");
			}
		});
		GridBagConstraints gbc_btnClear = new GridBagConstraints();
		gbc_btnClear.insets = new Insets(0, 0, 5, 0);
		gbc_btnClear.anchor = GridBagConstraints.NORTHEAST;
		gbc_btnClear.gridx = 0;
		gbc_btnClear.gridy = 2;
		getContentPane().add(btnClear, gbc_btnClear);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 0);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 3;
		getContentPane().add(tabbedPane, gbc_tabbedPane);

		j2jPanel = new JPanel();
		GridBagConstraints gbc_exportToJavaPanel = new GridBagConstraints();
		gbc_exportToJavaPanel.fill = GridBagConstraints.BOTH;
		gbc_exportToJavaPanel.gridx = 0;
		gbc_exportToJavaPanel.gridy = 1;
		getContentPane().add(j2jPanel, gbc_exportToJavaPanel);
		GridBagLayout gbl_exportToJavaPanel = new GridBagLayout();
		gbl_exportToJavaPanel.columnWeights = new double[] { 0.0, 0.5, 0.0, 1.0 };
		gbl_exportToJavaPanel.rowWeights = new double[] { 0.0 };
		j2jPanel.setLayout(gbl_exportToJavaPanel);

		lblNewLabel_2 = new JLabel("Package Name");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_2.insets = new Insets(5, 5, 5, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 0;
		j2jPanel.add(lblNewLabel_2, gbc_lblNewLabel_2);

		j2jPackageTextField = new JTextField();
		j2jPackageTextField.setText("com.fieldnation.data.");
		GridBagConstraints gbc_j2jPackageTextField = new GridBagConstraints();
		gbc_j2jPackageTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_j2jPackageTextField.anchor = GridBagConstraints.NORTHWEST;
		gbc_j2jPackageTextField.insets = new Insets(5, 5, 5, 5);
		gbc_j2jPackageTextField.gridx = 1;
		gbc_j2jPackageTextField.gridy = 0;
		j2jPanel.add(j2jPackageTextField, gbc_j2jPackageTextField);
		j2jPackageTextField.setColumns(20);

		lblNewLabel_3 = new JLabel("Export Path");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_3.insets = new Insets(5, 5, 5, 5);
		gbc_lblNewLabel_3.gridx = 2;
		gbc_lblNewLabel_3.gridy = 0;
		j2jPanel.add(lblNewLabel_3, gbc_lblNewLabel_3);

		j2jOutTextField = new JTextField();
		j2jOutTextField.setText("c:\\j2j\\out");
		GridBagConstraints gbc_j2jOutTextField = new GridBagConstraints();
		gbc_j2jOutTextField.weightx = 1.0;
		gbc_j2jOutTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_j2jOutTextField.insets = new Insets(5, 5, 5, 5);
		gbc_j2jOutTextField.anchor = GridBagConstraints.NORTHWEST;
		gbc_j2jOutTextField.gridx = 3;
		gbc_j2jOutTextField.gridy = 0;
		j2jPanel.add(j2jOutTextField, gbc_j2jOutTextField);
		j2jOutTextField.setColumns(30);

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

	public String getHttpMethod() {
		return ((String) methodComboBox.getSelectedItem()).toUpperCase();
	}

	public String getUrl() {
		return urlTextField.getText();
	}

	public String getUsername() {
		return usernameTextField.getText();
	}

	public String getPassword() {
		return new String(passwordTextField.getPassword());
	}

	public String getMIME() {
		return (String) mimeContentType.getSelectedItem();
	}

	public String getPostData() {
		return postDataTextArea.getText();
	}

	public void log(String msg) {
		try {
			JsonObject jobj = new JsonObject(msg);
			logTextArea.append(jobj.display() + "\n");
		} catch (Exception e) {
			try {
				JsonArray ja = new JsonArray(msg);
				log(ja.display());
			} catch (Exception ex2) {
				logTextArea.append(msg + "\n");
			}
		}
		logTextArea.setCaretPosition(logTextArea.getText().length());
	}

	public boolean isJ2JChecked() {
		return j2jChkBox.isSelected();
	}

	public String getJ2JPackage() {
		return j2jPackageTextField.getText();
	}

	public String getJ2JOut() {
		return j2jOutTextField.getText();
	}

}
