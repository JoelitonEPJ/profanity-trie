# import matplotlib.pyplot as plt
# import numpy as np

# x = np.linspace(0, 10, 100)
# y = np.sin(x)

# fig, ax = plt.subplots()
# ax.plot(x, y)

# ax.minorticks_on()
# minor_ticks = ax.yaxis.get_majorticklocs()

# for ytick in minor_ticks:
#     ax.axhline(y=ytick, color=["white", 'lightgray'], linewidth=0.5)

# plt.show()

import matplotlib.pyplot as plt
import numpy as np
x = np.linspace(0, 10, 100)
y = np.sin(x)
line, = plt.plot(x, y)
line.set_dashes([1, 5])  # 1pt line, 5pt space (creates spaced dots)
plt.show()   