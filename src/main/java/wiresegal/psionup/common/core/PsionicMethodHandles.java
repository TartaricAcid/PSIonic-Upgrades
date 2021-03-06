package wiresegal.psionup.common.core;

import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.logging.log4j.Level;
import vazkii.psi.common.block.tile.container.slot.SlotBullet;
import wiresegal.psionup.common.PsionicUpgrades;
import wiresegal.psionup.common.lib.LibObfuscation;

import javax.annotation.Nonnull;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static java.lang.invoke.MethodHandles.publicLookup;

/**
 * @author WireSegal
 *         Created at 10:43 PM on 7/8/16.
 */
public class PsionicMethodHandles {
    @Nonnull
    private static final MethodHandle socketSlotGetter, registerPotionTypeConversion;

    static {
        try {
            Field f = ReflectionHelper.findField(SlotBullet.class, "socketSlot");
            socketSlotGetter = publicLookup().unreflectGetter(f);
            Method m = ReflectionHelper.findMethod(PotionHelper.class, null, LibObfuscation.POTIONHELPER_REGISTERPOTIONTYPECONVERSION,
                    PotionType.class, Predicate.class, PotionType.class);
            registerPotionTypeConversion = publicLookup().unreflect(m);
        } catch (Throwable t) {
            PsionicUpgrades.Companion.getLOGGER().log(Level.ERROR, "Couldn't initialize methodhandles! Things will be broken!");
            t.printStackTrace();
            throw Throwables.propagate(t);
        }
    }

    public static int getSocketSlot(@Nonnull SlotBullet bullet) {
        try {
            return (int) socketSlotGetter.invokeExact(bullet);
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    public static void registerPotionTypeConversion(@Nonnull PotionType input, @Nonnull Predicate<ItemStack> reagentPredicate, @Nonnull PotionType output) {
        try {
            registerPotionTypeConversion.invokeExact(input, reagentPredicate, output);
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    private static RuntimeException propagate(Throwable t) {
        PsionicUpgrades.Companion.getLOGGER().log(Level.ERROR, "Methodhandle failed!");
        t.printStackTrace();
        return Throwables.propagate(t);
    }
}
