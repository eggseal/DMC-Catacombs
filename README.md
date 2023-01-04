# DMC Catacombs Plugin
## Setup
- Put the plugin `.jar` into your `plugins` folder on the server files
- After loading the plugin and generating a `config.yml` file, make sure to fill the values `catacomb-world-UID` and `default-world-UID`
**Note:** Most of the plugin's features won't work if these values are not set correctly

## Config
The configurable parts of the plugin 
| Config Keys | Value Types | Default Value | Description |
| - | - | - | - |
| `catacomb-world-UID` | Text/UUID | `null` | The UUID of the world to set as a 'Catacombs World' |
| `default-world-UID` | Text/UUID | `null` | The UUID of the world to set as a 'Default World'. Cannot have the same value as `catacomb-world-UID` |
| `run-length` | Number/Integer | `3600` | The amount of time in seconds that a player gets after being sent to a 'Catacombs World' to complete the run. A run is considered completed once a **Command Block** runs the `/catacomb winner` command |
| `loot-length` | Number/Integer | `900` | The amount of time in seconds that a player gets after completing a run on a 'Catacombs World' to grab as much of the reward as they want. Once this time runs out or the `/catacomb end` command gets executed, the player will be sent to the 'Default World' |
| `cooldown-length` | Number/Integer | `86400` | The amount of time in seconds a player has to wait between starting catacomb runs |
| `entrance-protection-radius` | Number/Integer | `15` | The radius of the grief protected area around each coordinate given in `entrance-list` |
| `entrance-list` | List of Text/Coordinates | `null` | A list of all coordinates that need to be protected from griefing, such as entrance points to a 'Catacomb World'. **Example:** To protect around the coordinates `x=10` `y=64` `z=-20` the value to add to the list is `'10 64 -20'` |
| `item-filter` | List of Text/ITEM_KEYS | `null` | A list of all the item keys to ignore when managing the reward pool. If a player dies/quits from a catacomb run all items from their inventory will be added to the prize pool except for the items in this list |