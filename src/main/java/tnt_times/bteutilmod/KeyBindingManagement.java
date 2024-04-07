package tnt_times.bteutilmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;

public class KeyBindingManagement implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        createBindings();
    }

    public static void createBindings(){
        ArrayList<KeyBinding> bindList = new ArrayList<>();
        ArrayList<String> commandsStrings = CommandsFileHandler.loadCommands();

        for(String text:commandsStrings){
            bindList.add(KeyBindingHelper.registerKeyBinding(new KeyBinding(text, -1, "BTE Util Mod")));
        }

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            for (KeyBinding b : bindList) {
                while (b.wasPressed()){
                    String text = b.getTranslationKey();

                    if (text.contains("[cp]")){
                        String cp = MinecraftClient.getInstance().keyboard.getClipboard();
                        text = text.replace("[cp]", cp);
                    }

                    if(text.contains("[ph]")){
                        String playerHeight = Double.toString(MinecraftClient.getInstance().player.getPos().y);
                        text = text.replace("[ph]", playerHeight);
                    }

                    if(text.startsWith("/")){
                        client.player.networkHandler.sendChatCommand(text.substring(1));
                    }else{
                        client.player.networkHandler.sendChatMessage(text);
                    }
                }
            }
        });
    }
}
