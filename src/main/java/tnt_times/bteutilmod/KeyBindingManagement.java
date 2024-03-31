package tnt_times.bteutilmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;

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
                    //client.player.sendMessage(Text.literal(b.getTranslationKey()), false);
                    //client.getServer().sendMessage(Text.literal(b.getTranslationKey()));
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
