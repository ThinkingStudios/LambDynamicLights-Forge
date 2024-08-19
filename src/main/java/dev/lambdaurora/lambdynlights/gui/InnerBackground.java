/*
 * Copyright © 2021 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of LambDynamicLights.
 *
 * Licensed under the Lambda License. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.lambdynlights.gui;

import org.thinkingstudio.obsidianui.background.Background;
import org.thinkingstudio.obsidianui.background.TransparentBackground;
import org.thinkingstudio.obsidianui.widget.SpruceWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class InnerBackground implements Background {
	@Override
	public void render(GuiGraphics graphics, SpruceWidget widget, int vOffset, int mouseX, int mouseY, float delta) {
		if (Minecraft.getInstance().level != null) {
			graphics.fillGradient(widget.getX(), widget.getY(),
					widget.getX() + widget.getWidth(), widget.getY() + widget.getHeight(),
					0xc0060606, 0xd0060606);
		} else {
			TransparentBackground.DARKENED.render(graphics, widget, vOffset, mouseX, mouseY, delta);
		}
	}
}
