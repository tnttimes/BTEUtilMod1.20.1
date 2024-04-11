package tnt_times.bteutilmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeyBindingManagement implements ClientModInitializer {

    private static KeyBinding openCommandsMenuKeyBind;

    @Override
    public void onInitializeClient() {
        openCommandsMenuKeyBind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.bteutilmod.open_menu",
                -1,
                "BTE Util Mod Menu"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
           while (openCommandsMenuKeyBind.wasPressed()){
               client.setScreen(new CommandsScreen(null));
           }
        });

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

                    if(text.contains("[ph")){
                        if(text.contains("[ph]")){
                            String playerHeight = Double.toString(MinecraftClient.getInstance().player.getPos().y);
                            text = text.replace("[ph]", playerHeight);
                        }else{
                            Pattern pattern = Pattern.compile("\\[ph(.*?)\\]");

                            Matcher matcher = pattern.matcher(text);

                            while (matcher.find()){
                                double heightOffset = Double.parseDouble(matcher.group(1).trim());
                                double playerHeight = MinecraftClient.getInstance().player.getPos().y;
                                text = text.replace("[ph".concat(matcher.group(1).trim()).concat("]"), Double.toString(playerHeight + heightOffset));
                            }
                        }
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
