package tnt_times.bteutilmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

public class KeyBindingsTest implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        KeyBinding bind1 = KeyBindingHelper.registerKeyBinding(new KeyBinding("bteutilmod.test_command_1", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M, "BTE Util Mod"));
        KeyBinding bind2 = KeyBindingHelper.registerKeyBinding(new KeyBinding("bteutilmod.test_command_2", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_N, "BTE Util Mod"));
        KeyBinding bind3 = KeyBindingHelper.registerKeyBinding(new KeyBinding("bteutilmod.test_command_3", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_B, "BTE Util Mod"));
        KeyBinding bind4 = KeyBindingHelper.registerKeyBinding(new KeyBinding("bteutilmod.test_command_4", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "BTE Util Mod"));

        ArrayList<KeyBinding> bindList = new ArrayList<>();

        bindList.add(bind1);
        bindList.add(bind2);
        bindList.add(bind3);
        bindList.add(bind4);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
                    for (KeyBinding b : bindList) {
                        while (b.wasPressed()){
                            client.player.sendMessage(Text.translatable(b.getTranslationKey()), false);
                        }
                    }
                });
    }
}
