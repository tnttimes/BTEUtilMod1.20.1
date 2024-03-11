package tnt_times.bteutilmod.mixin;

import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.*;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.option.GameOptions;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import tnt_times.bteutilmod.CommandsScreen;

import java.util.function.Supplier;

@Mixin(OptionsScreen.class)
public class MixinOptionsScreen extends Screen {
    @Mutable
    @Final
    @Shadow
    private GameOptions settings;

    @Mutable
    @Final
    @Shadow
    private Screen parent;

    public MixinOptionsScreen(Screen parent, GameOptions gameOptions) {
        super(Text.translatable("options.title"));
        this.parent = parent;
        this.settings = gameOptions;
    }

    @Shadow
    private void refreshResourcePacks(ResourcePackManager resourcePackManager) {}

    @Final
    @Shadow
    private static Text SKIN_CUSTOMIZATION_TEXT;
    @Final
    @Shadow
    private static Text SOUNDS_TEXT;
    @Final
    @Shadow
    private static Text VIDEO_TEXT;
    @Final
    @Shadow
    private static Text CONTROL_TEXT;
    @Final
    @Shadow
    private static Text LANGUAGE_TEXT;
    @Final
    @Shadow
    private static Text CHAT_TEXT;
    @Final
    @Shadow
    private static Text RESOURCE_PACK_TEXT;
    @Final
    @Shadow
    private static Text ACCESSIBILITY_TEXT;
    @Final
    @Shadow
    private static Text TELEMETRY_TEXT;
    @Final
    @Shadow
    private static Text CREDITS_AND_ATTRIBUTION_TEXT;

    private static final Text COMMANDS_TEXT = Text.translatable("gui.bteutilmod.commands_menu");


    @Override
    public void init() {
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().marginX(5).marginBottom(4).alignHorizontalCenter();
        GridWidget.Adder adder = gridWidget.createAdder(2);
        adder.add(this.settings.getFov().createWidget(this.client.options, 0, 0, 150));
        adder.add(this.createTopRightButton());

        adder.add(ButtonWidget.builder(COMMANDS_TEXT, button -> this.client.setScreen(new CommandsScreen(this))).width(310).build(), 2);

        adder.add(this.createButton(SKIN_CUSTOMIZATION_TEXT, () -> new SkinOptionsScreen(this, this.settings)));
        adder.add(this.createButton(SOUNDS_TEXT, () -> new SoundOptionsScreen(this, this.settings)));
        adder.add(this.createButton(VIDEO_TEXT, () -> new VideoOptionsScreen(this, this.settings)));
        adder.add(this.createButton(CONTROL_TEXT, () -> new ControlsOptionsScreen(this, this.settings)));
        adder.add(this.createButton(LANGUAGE_TEXT, () -> new LanguageOptionsScreen(this, this.settings, this.client.getLanguageManager())));
        adder.add(this.createButton(CHAT_TEXT, () -> new ChatOptionsScreen(this, this.settings)));
        adder.add(this.createButton(RESOURCE_PACK_TEXT, () -> new PackScreen(this.client.getResourcePackManager(), this::refreshResourcePacks, this.client.getResourcePackDir(), Text.translatable("resourcePack.title"))));
        adder.add(this.createButton(ACCESSIBILITY_TEXT, () -> new AccessibilityOptionsScreen(this, this.settings)));
        adder.add(this.createButton(TELEMETRY_TEXT, () -> new TelemetryInfoScreen(this, this.settings)));
        adder.add(this.createButton(CREDITS_AND_ATTRIBUTION_TEXT, () -> new CreditsAndAttributionScreen(this)));
        adder.add(ButtonWidget.builder(ScreenTexts.DONE, button -> this.client.setScreen(this.parent)).width(200).build(), 2, adder.copyPositioner().marginTop(6));
        gridWidget.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget, 0, this.height / 6 - 12, this.width, this.height, 0.5f, 0.0f);
        gridWidget.forEachChild(super::addDrawableChild);
    }

    private ButtonWidget createButton(Text message, Supplier<Screen> screenSupplier) {
        return ButtonWidget.builder(message, button -> this.client.setScreen((Screen)screenSupplier.get())).build();
    }
    @Shadow
    private CyclingButtonWidget<Difficulty> difficultyButton;

    @Shadow
    private LockButtonWidget lockDifficultyButton;

    @Shadow
    private void lockDifficulty(boolean difficultyLocked){}

    private Widget createTopRightButton() {
        if (this.client.world != null && this.client.isIntegratedServerRunning()) {
            this.difficultyButton = OptionsScreen.createDifficultyButtonWidget(0, 0, "options.difficulty", this.client);
            if (!this.client.world.getLevelProperties().isHardcore()) {
                this.lockDifficultyButton = new LockButtonWidget(0, 0, button -> this.client.setScreen(new ConfirmScreen(this::lockDifficulty, Text.translatable("difficulty.lock.title"), Text.translatable("difficulty.lock.question", this.client.world.getLevelProperties().getDifficulty().getTranslatableName()))));
                this.difficultyButton.setWidth(this.difficultyButton.getWidth() - this.lockDifficultyButton.getWidth());
                this.lockDifficultyButton.setLocked(this.client.world.getLevelProperties().isDifficultyLocked());
                this.lockDifficultyButton.active = !this.lockDifficultyButton.isLocked();
                this.difficultyButton.active = !this.lockDifficultyButton.isLocked();
                AxisGridWidget axisGridWidget = new AxisGridWidget(150, 0, AxisGridWidget.DisplayAxis.HORIZONTAL);
                axisGridWidget.add(this.difficultyButton);
                axisGridWidget.add(this.lockDifficultyButton);
                return axisGridWidget;
            }
            this.difficultyButton.active = false;
            return this.difficultyButton;
        }
        return ButtonWidget.builder(Text.translatable("options.online"), button -> this.client.setScreen(OnlineOptionsScreen.create(this.client, this, this.settings))).dimensions(this.width / 2 + 5, this.height / 6 - 12 + 24, 150, 20).build();
    }
}
