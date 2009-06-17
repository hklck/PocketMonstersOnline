package org.pokenet.client.ui.frames;

import java.util.ArrayList;
import java.util.List;

import mdes.slick.sui.Button;
import mdes.slick.sui.Frame;
import mdes.slick.sui.Label;
import mdes.slick.sui.event.ActionEvent;
import mdes.slick.sui.event.ActionListener;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;
import org.pokenet.client.GameClient;
import org.pokenet.client.ui.base.ConfirmationDialog;
import org.pokenet.client.ui.base.ListBox;

public class SpriteChooserDialog extends Frame {
	protected ListBox m_spriteList;
	protected Label m_spriteDisplay;
	protected String mustLoadSprite;
	private List<String> m_sprites;

	public SpriteChooserDialog() {
		getContentPane().setX(getContentPane().getX() - 1);
		getContentPane().setY(getContentPane().getY() + 1);
		m_sprites = new ArrayList<String>();
		for (int i = 1; i <= 218; i++) {
			m_sprites.add(String.valueOf(i));
		}

		m_spriteDisplay = new Label();
		m_spriteDisplay.setSize(124, 204);
		m_spriteDisplay.setLocation(105, 20);
		getContentPane().add(m_spriteDisplay);

		m_spriteList = new ListBox(m_sprites, false) {
			@Override
			protected void itemClicked(String itemName, int idx) {
				super.itemClicked(itemName, idx);
				mustLoadSprite = "res/characters/" + itemName + ".png";
			}
		};
		m_spriteList.setSize(105, 317);
		getContentPane().add(m_spriteList);

		setTitle("Please choose your character..");
		getCloseButton().setVisible(false);
		setSize(265, 340);
		setResizable(false);
		setDraggable(false);
		setVisible(true);
		initUse();
	}

	public void initUse() {
		final SpriteChooserDialog thisDialog = this;

		Button use = new Button("Use new sprite!");
		use.pack();
		use.setLocation(130, 245);
		getContentPane().add(use);

		Button cancel = new Button("Cancel");
		cancel.pack();
		cancel.setLocation(130, 280);
		getContentPane().add(cancel);

		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GameClient.getInstance().getDisplay().remove(thisDialog);
			}
		});

		use.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GameClient.getInstance().getDisplay().remove(thisDialog);

				final ConfirmationDialog confirm = new ConfirmationDialog("Are you sure you want to change sprites?\nIt'll cost you P1000!");
				confirm.addYesListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						confirm.setVisible(false);
						GameClient.getInstance().getDisplay().remove(confirm);

						GameClient.getInstance().getPacketGenerator().write(
								"S" + m_spriteList.getSelectedName());
					}
				});
				confirm.addNoListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						confirm.setVisible(false);
						GameClient.getInstance().getDisplay().remove(confirm);
					}
				});

				GameClient.getInstance().getDisplay().add(confirm);
			}
		});
	}

	public int getChoice() {
		return m_spriteList.getSelectedIndex();
	}

	@Override
	public void render(GUIContext container, Graphics g) {
		super.render(container, g);
		if (mustLoadSprite != null) {
			try {
				m_spriteDisplay.setImage(new Image(mustLoadSprite));
			} catch (SlickException e) {
				e.printStackTrace();
			}
			mustLoadSprite = null;
		}
	}
}