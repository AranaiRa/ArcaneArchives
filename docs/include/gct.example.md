### Examples

```java
import mods.arcanearchives.GCT;

// Removes the recipe for radiant dust
GCT.removeRecipe(<arcanearchives:radiant_dust>*2);

// Adds a new recipe for radiant dust
GCT.addRecipe("radiant_dust", <arcanearchives:radiant_dust>*2, [<minecraft:flint>, <arcanearchives:raw_quartz>]);

// Replaces the shaped radiant quartz recipe without disordering the GCT screen
GCT.replaceRecipe("shaped_quartz", <arcanearchives:shaped_quartz>, [<arcanearchives:raw_quartz>*10]);
```