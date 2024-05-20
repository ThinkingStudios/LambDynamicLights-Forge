/*
 * Copyright © 2020~2024 LambdAurora <email@lambdaurora.dev>
 * Copyright © 2024 ThinkingStudio
 *
 * This file is part of RyoamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.ryoamiclights.gui;

import org.thinkingstudio.obsidianui.Position;
import org.thinkingstudio.obsidianui.background.Background;
import org.thinkingstudio.obsidianui.background.EmptyBackground;
import org.thinkingstudio.obsidianui.background.SimpleColorBackground;
import org.thinkingstudio.obsidianui.navigation.NavigationDirection;
import org.thinkingstudio.obsidianui.navigation.NavigationUtils;
import org.thinkingstudio.obsidianui.widget.AbstractSpruceWidget;
import org.thinkingstudio.obsidianui.widget.SpruceLabelWidget;
import org.thinkingstudio.obsidianui.widget.SpruceWidget;
import org.thinkingstudio.obsidianui.widget.WithBackground;
import org.thinkingstudio.obsidianui.widget.container.SpruceEntryListWidget;
import org.thinkingstudio.obsidianui.widget.container.SpruceParentWidget;
import org.thinkingstudio.ryoamiclights.accessor.DynamicLightHandlerHolder;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class LightSourceListWidget extends SpruceEntryListWidget<LightSourceListWidget.LightSourceEntry> {
	private static final Background HIGHLIGHT_BACKGROUND = new SimpleColorBackground(128, 128, 128, 24);
	private int lastIndex = 0;

	public LightSourceListWidget(Position position, int width, int height) {
		super(position, width, height, 4, LightSourceEntry.class);
	}

	/**
	 * Adds a single option entry. The option will use all the width available.
	 *
	 * @param holder the option
	 * @return the index of the added entry
	 */
	public int addEntry(DynamicLightHandlerHolder<?> holder) {
		if (holder.ryoamiclights$getSetting() != null) {
			var entry = LightSourceEntry.create(this, holder);
			int index = this.addEntry(entry);
			if (index % 2 != 0)
				entry.setBackground(HIGHLIGHT_BACKGROUND);
			return index;
		}
		return -1;
	}

	public void addAll(List<DynamicLightHandlerHolder<?>> types) {
		for (var type : types)
			this.addEntry(type);
	}

	/* Narration */

	@Override
	public void appendNarrations(NarrationMessageBuilder builder) {
		this.children()
				.stream()
				.filter(AbstractSpruceWidget::isMouseHovered)
				.findFirst()
				.ifPresentOrElse(
						hoveredEntry -> {
							hoveredEntry.appendNarrations(builder.nextMessage());
							this.appendPositionNarrations(builder, hoveredEntry);
						}, () -> {
							var focusedEntry = this.getFocused();
							if (focusedEntry != null) {
								focusedEntry.appendNarrations(builder.nextMessage());
								this.appendPositionNarrations(builder, focusedEntry);
							}
						}
				);

		builder.put(NarrationPart.USAGE, Text.translatable("narration.component_list.usage"));
	}

	public static class LightSourceEntry extends Entry implements SpruceParentWidget<SpruceWidget>, WithBackground {
		private final List<SpruceWidget> children = new ArrayList<>();
		private final LightSourceListWidget parent;
		private @Nullable SpruceWidget focused;
		private boolean dragging;
		private Background background = EmptyBackground.EMPTY_BACKGROUND;

		private LightSourceEntry(LightSourceListWidget parent) {
			this.parent = parent;
		}

		public static LightSourceEntry create(LightSourceListWidget parent, DynamicLightHandlerHolder<?> option) {
			var entry = new LightSourceEntry(parent);
			var setting = option.ryoamiclights$getSetting();
			entry.children.add(new SpruceLabelWidget(Position.of(entry, entry.getWidth() / 2 - 155, 7), option.ryoamiclights$getName(), 175));
			entry.children.add(setting.getOption().createWidget(Position.of(entry, entry.getWidth() / 2 + 60, 2), 75));
			return entry;
		}

		@Override
		public int getWidth() {
			return this.parent.getWidth() - (this.parent.getBorder().getThickness() * 2);
		}

		@Override
		public int getHeight() {
			return this.children.stream().mapToInt(SpruceWidget::getHeight).reduce(Integer::max).orElse(0) + 4;
		}

		@Override
		public List<SpruceWidget> children() {
			return this.children;
		}

		@Override
		public @Nullable SpruceWidget getFocused() {
			return this.focused;
		}

		@Override
		public void setFocused(@Nullable SpruceWidget focused) {
			if (this.focused == focused)
				return;
			if (this.focused != null)
				this.focused.setFocused(false);
			this.focused = focused;
		}

		@Override
		public void setFocused(boolean focused) {
			super.setFocused(focused);
			if (!focused) {
				this.setFocused(null);
			}
		}

		@Override
		public Background getBackground() {
			return this.background;
		}

		@Override
		public void setBackground(Background background) {
			this.background = background;
		}

		/* Input */

		@Override
		protected boolean onMouseClick(double mouseX, double mouseY, int button) {
			var it = this.iterator();

			SpruceWidget element;
			do {
				if (!it.hasNext()) {
					return false;
				}

				element = it.next();
			} while (!element.mouseClicked(mouseX, mouseY, button));

			this.setFocused(element);
			if (button == GLFW.GLFW_MOUSE_BUTTON_1)
				this.dragging = true;

			return true;
		}

		@Override
		protected boolean onMouseRelease(double mouseX, double mouseY, int button) {
			this.dragging = false;
			return this.hoveredElement(mouseX, mouseY).filter(element -> element.mouseReleased(mouseX, mouseY, button)).isPresent();
		}

		@Override
		protected boolean onMouseDrag(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
			return this.getFocused() != null && this.dragging && button == GLFW.GLFW_MOUSE_BUTTON_1
					&& this.getFocused().mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
		}

		@Override
		protected boolean onKeyPress(int keyCode, int scanCode, int modifiers) {
			return this.focused != null && this.focused.keyPressed(keyCode, scanCode, modifiers);
		}

		@Override
		protected boolean onKeyRelease(int keyCode, int scanCode, int modifiers) {
			return this.focused != null && this.focused.keyReleased(keyCode, scanCode, modifiers);
		}

		@Override
		protected boolean onCharTyped(char chr, int keyCode) {
			return this.focused != null && this.focused.charTyped(chr, keyCode);
		}

		/* Rendering */

		protected void renderWidget(DrawContext graphics, int mouseX, int mouseY, float delta) {
			this.forEach(widget -> widget.render(graphics, mouseX, mouseY, delta));
		}

		protected void renderBackground(DrawContext graphics, int mouseX, int mouseY, float delta) {
			this.background.render(graphics, this, 0, mouseX, mouseY, delta);
		}

		/* Narration */

		@Override
		public void appendNarrations(NarrationMessageBuilder builder) {
			var focused = this.getFocused();
			if (focused != null) focused.appendNarrations(builder);
		}

		/* Navigation */

		@Override
		public boolean onNavigation(NavigationDirection direction, boolean tab) {
			if (this.requiresCursor()) return false;
			if (!tab && direction.isVertical()) {
				if (this.isFocused()) {
					this.setFocused(null);
					return false;
				}
				int lastIndex = this.parent.lastIndex;
				if (lastIndex >= this.children.size())
					lastIndex = this.children.size() - 1;
				if (!this.children.get(lastIndex).onNavigation(direction, tab))
					return false;
				this.setFocused(this.children.get(lastIndex));
				return true;
			}

			boolean result = NavigationUtils.tryNavigate(direction, tab, this.children, this.focused, this::setFocused, true);
			if (result) {
				this.setFocused(true);
				if (direction.isHorizontal() && this.getFocused() != null) {
					this.parent.lastIndex = this.children.indexOf(this.getFocused());
				}
			}
			return result;
		}
	}
}
