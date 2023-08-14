from json import dump

level_0_xp = 30
number_of_levels = 16
levels = []

for x in range(0, number_of_levels):
	levels.append({
		"level": x,
		"xp": level_0_xp + (x * level_0_xp)
	})

dump(levels, open("app/src/main/resources/levels.json", "w"))
