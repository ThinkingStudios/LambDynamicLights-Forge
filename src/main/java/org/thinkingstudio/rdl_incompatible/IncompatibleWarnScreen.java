package org.thinkingstudio.rdl_incompatible;

import dev.architectury.platform.Platform;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.WarningScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import org.thinkingstudio.rdl_incompatible.mixin.WarningScreenAccessor;

import java.io.File;

public class IncompatibleWarnScreen extends WarningScreen {
    protected IncompatibleWarnScreen() {
        super(HEADER, MESSAGE, CHECK_MESSAGE, NARRATED_TEXT);
    }

    @Override
    protected void initButtons(int yOffset) {
        addDrawableChild(new ButtonWidget(width / 2 - 155, 100 + yOffset, 150, 20, OPEN_MODS_FOLDER, buttonWidget ->  {
            Util.getOperatingSystem().open(new File(Platform.getModsFolder().toFile(), "mods"));
        }));

        addDrawableChild(new ButtonWidget(width / 2 - 155 + 160, 100 + yOffset, 150, 20, AN_ISSUES_PAGE, buttonWidget ->  {
            Util.getOperatingSystem().open("https://github.com/ThinkingStudios/LambDynamicLights-Forge/issues/3");
        }));

        if(RDLIncompatible.CONFIG.allowToProceed) {
            addDrawableChild(new ButtonWidget(width / 2 - 75, 130 + yOffset, 150, 20, Text.translatable("label.rdlincompatible.proceed"), buttonWidget ->  {
                if(checkbox.isChecked()) {
                    if(RDLIncompatible.HAS_AN) {
                        RDLIncompatible.CONFIG.showArsNouveauScreen = false;
                    }
                    RDLIncompatible.saveConfig(RDLIncompatible.CONFIG);
                }
                client.setScreen(new TitleScreen(false));
            }));
        }
    }

    @Override
    protected void init() {
        ((WarningScreenAccessor) this).setMultilineMessage(MultilineText.create(textRenderer, MESSAGE, width - 50));
        int yOffset = (((WarningScreenAccessor) this).getMultilineMessage().count() + 1) * textRenderer.fontHeight * 2 - 20;
        if(RDLIncompatible.CONFIG.allowToProceed) {
            checkbox = new CheckboxWidget(width / 2 - 155 + 80, 76 + yOffset, 150, 20, CHECK_MESSAGE, false);
            addDrawableChild(checkbox);
        }
        initButtons(yOffset);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    private static final MutableText HEADER = Text.translatable("header.rdlincompatible.incompatible").formatted(Formatting.DARK_RED, Formatting.BOLD);
    private static final Text MESSAGE = Text.translatable("message.rdlincompatible.incompatible");
    private static final Text CHECK_MESSAGE = Text.translatable("multiplayerWarning.check");
    private static final MutableText NARRATED_TEXT = HEADER.copy().append("\n").append(MESSAGE);

    private static final Text OPEN_MODS_FOLDER = Text.translatable("label.rdlincompatible.open_mods_folder");
    private static final Text AN_ISSUES_PAGE = Text.translatable("label.rdlincompatible.an_issues");
}
