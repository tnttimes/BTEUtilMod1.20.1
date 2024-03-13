package tnt_times.bteutilmod;

import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.Components;
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

public class CommandsScreen extends BaseOwoScreen<FlowLayout> {

    private final Screen parent;

    private final FlowLayout commandsContainer = Containers.verticalFlow(Sizing.fill(100), Sizing.content());

    private ArrayList<String> commands;

    public CommandsScreen(Screen parentScreen){
        this.parent = parentScreen;
    }

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        commands = CommandsFileHandler.loadCommands();
        rootComponent.surface(Surface.flat(Color.ofDye(DyeColor.BLACK).argb()))
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.TOP)
                .padding(Insets.of(10,0,0,0))
        ;
        rootComponent.child(
                Components.label(Text.translatable("gui.bteutilmod.commands_menu"))
        );

        rootComponent.child(
          Components.button(Text.translatable("gui.bteutilmod.add_command").setStyle(Style.EMPTY.withColor(Formatting.GREEN)), buttonComponent -> {
              addCommandBoxToContainer(commandsContainer);
          })
                  .horizontalSizing(Sizing.fixed(170))
                  .margins(Insets.of(8,8,0,0))
        );

        rootComponent.child(
                Containers.verticalScroll(Sizing.fill(70), Sizing.fill(60),
                        commandsContainer
                                .padding(Insets.of(0,0,5,5))
                                .id("commands_container")
                )
        );

        rootComponent.child(
                Containers.verticalFlow(Sizing.fill(100), Sizing.fill(15))
                        .child(
                            Components.button(Text.translatable("gui.done"), buttonComponent -> {
                                CommandsFileHandler.saveCommands(commands);
                                MinecraftClient.getInstance().setScreen(parent);
                            })
                                    .horizontalSizing(Sizing.fixed(200))
                        )
                        .surface(Surface.flat(Color.ofDye(DyeColor.GRAY).argb()))
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .verticalAlignment(VerticalAlignment.CENTER)
                        .positioning(Positioning.relative(50, 100))
        );
    }

    private final List<FlowLayout> commandsList = new ArrayList<>();
    private int sessionCommandID = 0;

    private void addCommandBoxToContainer(FlowLayout container){
        final int x = sessionCommandID;

        commandsList.add(
                Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                        .child(
                                Components.textBox(Sizing.fill(88)).margins(Insets.of(0,7,0,0))
                        )
                        .child(
                                Components.button(Text.literal("X").setStyle(Style.EMPTY.withColor(Formatting.RED)), buttonComponent -> {
                                            removeCommandBox(commandsContainer, x);
                                        })
                                        .positioning(Positioning.relative(100, 0))
                        )
        );
        container.child(commandsList.get(commandsList.size() - 1));
        commandsList.get(commandsList.size() - 1).id(Integer.toString(sessionCommandID));
        sessionCommandID++;
    }

    private void removeCommandBox(FlowLayout container, int id){
        for (FlowLayout layout:commandsList) {
            if(layout.id() != null){
                if(Integer.parseInt(layout.id()) == id){
                    container.removeChild(layout);
                    commandsList.remove(layout);
                    break;
                }
            }
        }
    }
}
