package tnt_times.bteutilmod;

import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ConfirmCommandsScreen extends BaseOwoScreen<FlowLayout> {
    private final Screen subParent;
    private final ArrayList<String> commands;
    private final List<TextBoxComponent> textBoxes;

    public ConfirmCommandsScreen(Screen parentScreen, ArrayList<String> commands, List<TextBoxComponent> textBoxes){
        this.subParent = parentScreen;
        this.commands = commands;
        this.textBoxes = textBoxes;
    }
    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.surface(Surface.flat(Color.ofDye(DyeColor.BLACK).argb()))
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER)
                .padding(Insets.of(10,0,0,0))
        ;
        rootComponent.child(
                Components.label(Text.translatable("gui.bteutilmod.confirm_changes"))
        );
        rootComponent.child(
                Components.button(Text.translatable("gui.yes").setStyle(Style.EMPTY.withColor(Formatting.GREEN)), buttonComponent -> {
                            save();
                        })
                        .horizontalSizing(Sizing.fixed(170))
                        .margins(Insets.of(8,8,0,0))
        );
        rootComponent.child(
                Components.button(Text.translatable("gui.no").setStyle(Style.EMPTY.withColor(Formatting.RED)), buttonComponent -> {
                            save();
                            MinecraftClient.getInstance().setScreen(new CommandsScreen(subParent));
                        })
                        .horizontalSizing(Sizing.fixed(170))
                        .margins(Insets.of(8,8,0,0))
        );
    }

    private void save(){
        commands.clear();
        for(TextBoxComponent c:textBoxes){
            commands.add(c.getText());
        }
        CommandsFileHandler.saveCommands(commands);
        MinecraftClient.getInstance().setScreen(subParent);
    }
}
