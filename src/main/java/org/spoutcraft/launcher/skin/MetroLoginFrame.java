/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
 * Spoutcraft is licensed under the SpoutDev License Version 1.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spoutcraft.launcher.skin;

import static org.spoutcraft.launcher.util.ResourceUtils.getResourceAsStream;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.spoutcraft.launcher.skin.components.BackgroundImage;
import org.spoutcraft.launcher.skin.components.DynamicButton;
import org.spoutcraft.launcher.skin.components.HyperlinkJLabel;
import org.spoutcraft.launcher.skin.components.ImageHyperlinkButton;
import org.spoutcraft.launcher.skin.components.LiteButton;
import org.spoutcraft.launcher.skin.components.LitePasswordBox;
import org.spoutcraft.launcher.skin.components.LiteProgressBar;
import org.spoutcraft.launcher.skin.components.LiteTextBox;
import org.spoutcraft.launcher.skin.components.LoginFrame;
import org.spoutcraft.launcher.skin.components.TransparentButton;
import org.spoutcraft.launcher.util.ImageUtils;
import org.spoutcraft.launcher.util.OperatingSystem;
import org.spoutcraft.launcher.util.ResourceUtils;

public class MetroLoginFrame extends LoginFrame implements ActionListener,
		KeyListener {
	private static final long serialVersionUID = 1L;
	private static final URL gearIcon = LoginFrame.class
			.getResource("/org/spoutcraft/launcher/resources/gear.png");
	private static final int FRAME_WIDTH = 880;
	private static final int FRAME_HEIGHT = 520;
	private static final String OPTIONS_ACTION = "options";
	private static final String LOGIN_ACTION = "login";
	private static final String IMAGE_LOGIN_ACTION = "image_login";
	private static final String REMOVE_USER = "remove";
	private final Map<JButton, DynamicButton> removeButtons = new HashMap<JButton, DynamicButton>();
	private LiteTextBox name;
	private LitePasswordBox pass;
	private LiteButton login;
	private JCheckBox remember;
	private TransparentButton options;
	private LiteProgressBar progressBar;
	private OptionsMenu optionsMenu = null;

	public MetroLoginFrame() {
		initComponents();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((dim.width - FRAME_WIDTH) / 2,
				(dim.height - FRAME_HEIGHT) / 2, FRAME_WIDTH, FRAME_HEIGHT);
		setResizable(false);
		getContentPane().add(new BackgroundImage(FRAME_WIDTH, FRAME_HEIGHT));
	}

	private void initComponents() {
		Font minecraft = getMinecraftFont(12);

		int xShift = 0;
		int yShift = 0;
		if (this.isUndecorated()) {
			yShift += 30;
		}

		// Setup username box
		name = new LiteTextBox(this, "Логин");
		name.setBounds(562 + xShift, 426 + yShift, 200, 24);
		name.setFont(minecraft);
		name.addKeyListener(this);

		// Setup password box
		pass = new LitePasswordBox(this, "Пароль (не обязательно)");
		pass.setBounds(562 + xShift, 455 + yShift, 200, 24);
		pass.setFont(minecraft);
		pass.addKeyListener(this);

		// Setup remember checkbox
		remember = new JCheckBox("Запомнить");
		remember.setBounds(770 + xShift, 455 + yShift, 110, 24);
		remember.setFont(minecraft);
		remember.setOpaque(false);
		remember.setBorderPainted(false);
		remember.setContentAreaFilled(false);
		remember.setBorder(null);
		remember.setForeground(Color.WHITE);
		remember.addKeyListener(this);

		// Setup login button
		login = new LiteButton("В игру!");
		login.setBounds(770 + xShift, 426 + yShift, 92, 24);
		login.setFont(minecraft);
		login.setActionCommand(LOGIN_ACTION);
		login.addActionListener(this);
		login.addKeyListener(this);

		// Spoutcraft logo
		JLabel logo = new JLabel();
		logo.setBounds(8, 15, 400, 109);
		setIcon(logo, "spoutcraft.png", logo.getWidth(), logo.getHeight());

		// Progress Bar
		progressBar = new LiteProgressBar();
		progressBar.setBounds(8, 150, 395, 23);
		progressBar.setVisible(true);
		progressBar.setStringPainted(true);
		progressBar.setOpaque(true);
		progressBar.setTransparency(1F);
		progressBar.setHoverTransparency(0.70F);
		progressBar.setFont(minecraft);

		// Home Link
		Font largerMinecraft;
		if (OperatingSystem.getOS().isUnix()) {
			largerMinecraft = minecraft.deriveFont((float) 16);
		} else {
			largerMinecraft = minecraft.deriveFont((float) 18);
		}

		HyperlinkJLabel home = new HyperlinkJLabel("Сайт",
				"http://www.spout.org/");
		home.setFont(largerMinecraft);
		home.setBounds(545, 35, 65, 20);
		home.setForeground(Color.WHITE);
		home.setOpaque(false);
		home.setTransparency(0.70F);
		home.setHoverTransparency(1F);

		// Forums link
		HyperlinkJLabel forums = new HyperlinkJLabel("Форум",
				"http://forums.spout.org/");
		forums.setFont(largerMinecraft);
		forums.setBounds(625, 35, 90, 20);
		forums.setForeground(Color.WHITE);
		forums.setOpaque(false);
		forums.setTransparency(0.70F);
		forums.setHoverTransparency(1F);

		// Issues link
		HyperlinkJLabel issues = new HyperlinkJLabel("Помощь",
				"http://spout.in/issues");
		issues.setFont(largerMinecraft);
		issues.setBounds(733, 35, 85, 20);
		issues.setForeground(Color.WHITE);
		issues.setOpaque(false);
		issues.setTransparency(0.70F);
		issues.setHoverTransparency(1F);

		// Options Button
		options = new TransparentButton();
		options.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				gearIcon)));
		options.setBounds(828, 28, 30, 30);
		options.setTransparency(0.70F);
		options.setHoverTransparency(1F);
		options.setActionCommand(OPTIONS_ACTION);
		options.addActionListener(this);

		// Steam button
		JButton steam = new ImageHyperlinkButton("http://spout.in/steam");
		steam.setToolTipText("Game with us on Steam");
		steam.setBounds(6, FRAME_HEIGHT - 62 + yShift, 28, 28);
		setIcon(steam, "steam.png", 28);

		// Twitter button
		JButton twitter = new ImageHyperlinkButton("http://twitter.com/_asn007");
		twitter.setToolTipText("Подпишись на меня в твиттере.\nНу позязя :3");
		twitter.setBounds(6 + 34 + xShift, FRAME_HEIGHT - 62 + yShift, 28, 28);
		setIcon(twitter, "twitter.png", 28);

		// Facebook button
		JButton rubukkit = new ImageHyperlinkButton(
				"http://www.rubukkit.org/members/dr-death.17933/");
		rubukkit.setToolTipText("Профиль на рубакките. Будь няшкой, пролайкай пару сообщений :3");
		rubukkit.setBounds(6, FRAME_HEIGHT - 62 + yShift, 28, 28);
		setIcon(rubukkit, "rubukkit.png", 28);

		// Google+ button
		JButton gplus = new ImageHyperlinkButton("http://spout.in/gplus");
		gplus.setToolTipText("Follow us on Google+");
		gplus.setBounds(6 + 34 * 2 + xShift, FRAME_HEIGHT - 62 + yShift, 28, 28);
		setIcon(gplus, "gplus.png", 28);

		// YouTube button
		JButton youtube = new ImageHyperlinkButton("http://spout.in/youtube");
		youtube.setToolTipText("Subscribe to our videos");
		youtube.setBounds(6 + 34 + xShift, FRAME_HEIGHT - 62 + yShift, 28, 28);
		setIcon(youtube, "youtube.png", 28);

		Container contentPane = getContentPane();
		contentPane.setLayout(null);

		java.util.List<String> savedUsers = getSavedUsernames();
		int users = Math.min(5, this.getSavedUsernames().size());
		for (int i = 0; i < users; i++) {
			String accountName = savedUsers.get(i);
			String userName = this.getUsername(accountName);

			DynamicButton userButton = new DynamicButton(this,
					getImage(userName), 44, accountName, userName);
			userButton.setFont(minecraft.deriveFont(14F));

			userButton.setBounds((FRAME_WIDTH - 75) * (i + 1) / (users + 1),
					(FRAME_HEIGHT - 75) / 2, 75, 75);
			contentPane.add(userButton);
			userButton.setActionCommand(IMAGE_LOGIN_ACTION);
			userButton.addActionListener(this);
			setIcon(userButton.getRemoveIcon(), "remove.png", 16);
			userButton.getRemoveIcon().addActionListener(this);
			userButton.getRemoveIcon().setActionCommand(REMOVE_USER);
			removeButtons.put(userButton.getRemoveIcon(), userButton);
		}

		contentPane.add(name);
		contentPane.add(pass);
		contentPane.add(remember);
		contentPane.add(login);
		// contentPane.add(steam);
		contentPane.add(twitter);
		contentPane.add(rubukkit);
		// contentPane.add(gplus);
		// contentPane.add(youtube);
		contentPane.add(home);
		contentPane.add(forums);
		contentPane.add(issues);
		contentPane.add(logo);
		contentPane.add(options);
		contentPane.add(progressBar);

		setFocusTraversalPolicy(new LoginFocusTraversalPolicy());
	}

	private void setIcon(JButton button, String iconName, int size) {
		try {
			button.setIcon(new ImageIcon(
					ImageUtils.scaleImage(
							ImageIO.read(ResourceUtils
									.getResourceAsStream("/org/spoutcraft/launcher/resources/"
											+ iconName)), size, size)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setIcon(JLabel label, String iconName, int w, int h) {
		try {
			label.setIcon(new ImageIcon(
					ImageUtils.scaleImage(
							ImageIO.read(ResourceUtils
									.getResourceAsStream("/org/spoutcraft/launcher/resources/"
											+ iconName)), w, h)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private BufferedImage getImage(String user) {
		try {
			URLConnection conn = (new URL("https://minotar.net/helm/" + user
					+ "/100")).openConnection();
			InputStream stream = conn.getInputStream();
			BufferedImage image = ImageIO.read(stream);
			if (image != null) {
				return image;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			return ImageIO
					.read(getResourceAsStream("/org/spoutcraft/launcher/resources/steve.png"));
		} catch (IOException e1) {
			throw new RuntimeException("Error reading backup image", e1);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JComponent) {
			action(e.getActionCommand(), (JComponent) e.getSource());
		}
	}

	private void action(String action, JComponent c) {
		if (action.equals(OPTIONS_ACTION)) {
			if (optionsMenu == null || !optionsMenu.isVisible()) {
				optionsMenu = new OptionsMenu();
				optionsMenu.setModal(true);
				optionsMenu.setVisible(true);
			}
		} else if (action.equals(LOGIN_ACTION)) {
			String pass = new String(this.pass.getPassword());
			if (getSelectedUser().length() > 0 && pass.length() > 0) {
				this.doLogin(getSelectedUser(), pass, this);
				if (remember.isSelected()) {
					saveUsername(getSelectedUser(), pass);
				}
			} else if (getSelectedUser().length() > 0 && pass.length() <= 0) {
				this.doLogin(getSelectedUser(), null, this);
				if (remember.isSelected()) {
					saveUsername(getSelectedUser(), "");
				}
			}
		} else if (action.equals(IMAGE_LOGIN_ACTION)) {
			DynamicButton userButton = (DynamicButton) c;
			this.name.setText(userButton.getAccount());
			this.pass.setText(this.getSavedPassword(userButton.getAccount()));
			this.remember.setSelected(true);
			action(LOGIN_ACTION, userButton);
		} else if (action.equals(REMOVE_USER)) {
			DynamicButton userButton = removeButtons.get((JButton) c);
			this.removeAccount(userButton.getAccount());
			userButton.setVisible(false);
			userButton.setEnabled(false);
			getContentPane().remove(userButton);
			c.setVisible(false);
			c.setEnabled(false);
			getContentPane().remove(c);
			removeButtons.remove(c);
			writeUsernameList();
		}
	}

	public void stateChanged(String status, float progress) {
		int intProgress = Math.round(progress);
		progressBar.setValue(intProgress);
		if (status.length() > 60) {
			status = status.substring(0, 60) + "...";
		}
		progressBar.setString(intProgress + "% " + status);
	}

	@Override
	public JProgressBar getProgressBar() {
		return progressBar;
	}

	@Override
	public void disableForm() {

	}

	@Override
	public void enableForm() {

	}

	@Override
	public String getSelectedUser() {
		return this.name.getText();
	}

	// Emulates tab focus policy of name -> pass -> remember -> login
	private class LoginFocusTraversalPolicy extends FocusTraversalPolicy {
		public Component getComponentAfter(Container con, Component c) {
			if (c == name) {
				return pass;
			} else if (c == pass) {
				return remember;
			} else if (c == remember) {
				return login;
			} else if (c == login) {
				return name;
			}
			return getFirstComponent(con);
		}

		public Component getComponentBefore(Container con, Component c) {
			if (c == name) {
				return login;
			} else if (c == pass) {
				return name;
			} else if (c == remember) {
				return pass;
			} else if (c == login) {
				return remember;
			}
			return getFirstComponent(con);
		}

		public Component getFirstComponent(Container c) {
			return name;
		}

		public Component getLastComponent(Container c) {
			return login;
		}

		public Component getDefaultComponent(Container c) {
			return name;
		}

	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			// Allows the user to press enter and log in from the login box
			// focus, username box focus, or password box focus
			if (e.getComponent() == login || e.getComponent() == name
					|| e.getComponent() == pass) {
				action(LOGIN_ACTION, (JComponent) e.getComponent());
			} else if (e.getComponent() == remember) {
				remember.setSelected(!remember.isSelected());
			}
		}
	}

	public void keyReleased(KeyEvent e) {
	}
}
