package jeresources.api.restrictions;

import jeresources.util.BiomeHelper;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class BiomeRestriction {
    public static final BiomeRestriction NO_RESTRICTION = new BiomeRestriction();
    
   public static final BiomeRestriction NONE = new BiomeRestriction(Biome.Category.NONE);
   public static final BiomeRestriction TAIGA = new BiomeRestriction(Biome.Category.TAIGA);
   public static final BiomeRestriction EXTREME_HILLS = new BiomeRestriction(Biome.Category.EXTREME_HILLS);
   public static final BiomeRestriction JUNGLE = new BiomeRestriction(Biome.Category.JUNGLE);
   public static final BiomeRestriction MESA = new BiomeRestriction(Biome.Category.MESA);
   public static final BiomeRestriction PLAINS = new BiomeRestriction(Biome.Category.PLAINS);
   public static final BiomeRestriction SAVANNA = new BiomeRestriction(Biome.Category.SAVANNA);
   public static final BiomeRestriction ICY = new BiomeRestriction(Biome.Category.ICY);
   public static final BiomeRestriction THEEND = new BiomeRestriction(Biome.Category.THEEND);
   public static final BiomeRestriction BEACH = new BiomeRestriction(Biome.Category.BEACH);
   public static final BiomeRestriction FOREST = new BiomeRestriction(Biome.Category.FOREST);
   public static final BiomeRestriction OCEAN = new BiomeRestriction(Biome.Category.OCEAN);
   public static final BiomeRestriction DESERT = new BiomeRestriction(Biome.Category.DESERT);
   public static final BiomeRestriction RIVER = new BiomeRestriction(Biome.Category.RIVER);
   public static final BiomeRestriction SWAMP = new BiomeRestriction(Biome.Category.SWAMP);
   public static final BiomeRestriction MUSHROOM = new BiomeRestriction(Biome.Category.MUSHROOM);
   public static final BiomeRestriction NETHER = new BiomeRestriction(Biome.Category.NETHER);

    private List<Biome> biomes = new ArrayList<>();
    private Restriction.Type restrictionType;

    public BiomeRestriction() {
        this.restrictionType = Restriction.Type.NONE;
    }

    public BiomeRestriction(Biome biome) {
        this(Restriction.Type.WHITELIST, biome);
    }

    public BiomeRestriction(Restriction.Type restrictionType, Biome biome) {
        this(restrictionType, biome, new Biome[0]);
    }

    public BiomeRestriction(Biome biome, Biome... moreBiomes) {
        this(Restriction.Type.WHITELIST, biome, moreBiomes);
    }

    public BiomeRestriction(Restriction.Type restrictionType, Biome biome, Biome... moreBiomes) {
        this.restrictionType = restrictionType;
        switch (restrictionType) {
            case NONE:
                break;
            case WHITELIST:
                this.biomes.add(biome);
                this.biomes.addAll(Arrays.asList(moreBiomes));
                break;
            default:
                biomes = BiomeHelper.getAllBiomes();
                biomes.remove(biome);
                biomes.removeAll(Arrays.asList(moreBiomes));
        }
    }

    public BiomeRestriction(Biome.Category biomeCategory, Biome.Category... biomeCategories) {
        this(Restriction.Type.WHITELIST, biomeCategory, biomeCategories);
    }

    public BiomeRestriction(Restriction.Type restrictionType, Biome.Category biomeCategory, Biome.Category... biomeCategories) {
        this.restrictionType = restrictionType;
        switch (restrictionType) {
            case NONE:
                break;
            case WHITELIST:
                biomes = getBiomes(biomeCategory, biomeCategories);
                break;
            default:
                biomes = BiomeHelper.getAllBiomes();
                biomes.removeAll(getBiomes(biomeCategory, biomeCategories));
        }
    }

    private ArrayList<Biome> getBiomes(Biome.Category biomeCategory, Biome.Category... biomeCategories) {
        ArrayList<Biome> biomes = new ArrayList<>();
        biomes.addAll(BiomeHelper.getBiomes(biomeCategory));
        for (int i = 1; i < biomeCategories.length; i++) {
            ArrayList<Biome> newBiomes = new ArrayList<>();
            for (Biome biome : BiomeHelper.getBiomes(biomeCategories[i])) {
                if (biomes.remove(biome)) newBiomes.add(biome); // intersection of all selected categories
            }
            biomes = newBiomes;
        }
        return biomes;
    }

    public List<String> toStringList() {
        return biomes.stream().filter(biome -> !biome.toString().equals("")).map(biome -> "  " + biome.toString()).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BiomeRestriction) {
            BiomeRestriction other = (BiomeRestriction) obj;
            return other.biomes.size() == biomes.size() && other.biomes.containsAll(biomes);
        }
        return false;
    }

    public boolean isMergeAble(BiomeRestriction other) {
        return other.restrictionType == Restriction.Type.NONE || (this.restrictionType != Restriction.Type.NONE && !biomes.isEmpty() && other.biomes.containsAll(biomes));
    }

    @Override
    public String toString() {
        return "Biomes: " + restrictionType + (restrictionType != Restriction.Type.NONE ? " - " + biomes.size() : "");
    }

    @Override
    public int hashCode() {
        return restrictionType.hashCode() ^ biomes.hashCode();
    }
}
