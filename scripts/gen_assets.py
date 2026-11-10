"""Generates placeholder textures, item models and lang for all 85 Marks.
Run from the project root: python3 scripts/gen_assets.py
To use real art, drop your own PNGs in src/main/resources/... after running,
or delete this script's output for the files you replace."""
import json, colorsys, os
from PIL import Image, ImageDraw
R = "src/main/resources/assets"
for d in ("ironman/models/item", "ironman/textures/item", "ironman/lang", "minecraft/textures/models/armor"):
    os.makedirs(f"{R}/{d}", exist_ok=True)
names = ["helmet", "chestplate", "leggings", "boots"]
lang = {"itemGroup.ironman.suits": "Iron Man Suits"}
def color(m):
    if m == 1: return (110, 110, 115)
    r, g, b = colorsys.hsv_to_rgb((m % 7) * 0.01, 0.85, 0.75)
    return (int(r*255), int(g*255), int(b*255))
for m in range(1, 86):
    base = color(m); gold = (230, 190, 40) if m != 1 else (90, 90, 95)
    for layer in (1, 2):
        p = f"{R}/minecraft/textures/models/armor/ironman_mark_{m}_layer_{layer}.png"
        if os.path.exists(p): continue
        im = Image.new("RGBA", (64, 32), base + (255,)); d = ImageDraw.Draw(im)
        for x in range(0, 64, 8): d.line([(x, 0), (x, 31)], fill=gold + (255,))
        im.save(p)
    for n in names:
        iid = f"mark_{m}_{n}"
        p = f"{R}/ironman/textures/item/{iid}.png"
        if not os.path.exists(p):
            im = Image.new("RGBA", (16, 16), (0, 0, 0, 0)); d = ImageDraw.Draw(im)
            d.rectangle([2, 2, 13, 13], fill=base + (255,), outline=gold + (255,))
            d.text((4, 3), str(m % 100), fill=(255, 255, 255, 255))
            im.save(p)
        json.dump({"parent": "item/generated", "textures": {"layer0": f"ironman:item/{iid}"}},
                  open(f"{R}/ironman/models/item/{iid}.json", "w"))
        lang[f"item.ironman.{iid}"] = f"Mark {m} {n.capitalize()}"
json.dump(lang, open(f"{R}/ironman/lang/en_us.json", "w"), indent=1)
