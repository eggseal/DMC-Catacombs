# DMC Catacombs Plugin

## Setup

- Put the plugin `.jar` into your `plugins` folder on the server files
- After loading the plugin and generating a `config.yml` file, make sure to fill the values `catacomb-world-UID` and `default-world-UID`
  **Note:** Most of the plugin's features won't work if these values are not set to valid world UUIDs

## Commands

### Command block only

Command blocks with these commands are supposed to be setup along the catacomb run so players can access them

- `/catacomb start` Starts a catacomb run for player nearest to the command block. Point of access between the 'Default World' and the 'Catacomb World'
- `/catacomb winner` Declares the current catacomb run as completed, meaning the player is now able to access the rewards and is no longer at risk of losing their items
- `/catacomb display_reward` Displays a chest GUI with all the accumulated rewards to the player if their run is completed
- `/catacomb end` Finishes the current run if this is completed, sending the player to the 'Default World'

### Console only

- `/catacomb start <player>` Starts a catacomb run for player. Point of access between the 'Default World' and the 'Catacomb World'

### Player only

These commands can only be accessed by players with the `earthcatacombs.catacomb`/`earthcatacombs.*` permission.

- `/catacomb filter <add/remove>` Add/Remove the held item's key to the `item-filter` config value
- `/catacomb default_reward <add/remove>` Add/Remove the held item to the `default-prize` item list

## Config Guide

| Config Keys                  | Value Types              | Default Value | Description                                                                                                                                                                                                                                                     |
| ---------------------------- | ------------------------ | ------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `catacomb-world-UID`         | Name/UUID                | `null`        | The UUID of the world to set as a 'Catacombs World'. Cannot be the same value as `default-world-UID`                                                                                                                                                            |
| `default-world-UID`          | Name/UUID                | `null`        | The UUID of the world to set as a 'Default World'. Cannot be the same value as `catacomb-world-UID`                                                                                                                                                             |
| `run-length`                 | Number/Integer           | `3600`        | The amount of time in seconds that a player gets after being sent to a 'Catacombs World' to complete the run. A run is considered completed once a **Command Block** runs the `/catacomb winner` command                                                        |
| `loot-length`                | Number/Integer           | `900`         | The amount of time in seconds that a player gets after completing a run on a 'Catacombs World' to grab as much of the reward as they want. Once this time runs out or the `/catacomb end` command gets executed, the player will be sent to the 'Default World' |
| `cooldown-length`            | Number/Integer           | `86400`       | The amount of time in seconds a player has to wait between starting catacomb runs                                                                                                                                                                               |
| `entrance-protection-radius` | Number/Integer           | `15`          | The radius of the grief protected area around each coordinate given in `entrance-list`                                                                                                                                                                          |
| `entrance-list`              | List of Text/Coordinates | `null`        | A list of all coordinates that need to be protected from griefing, such as entrance points to a 'Catacomb World'. **Example:** To protect around the coordinates `x=10` `y=64` `z=-20` the value to add to the list is `'10 64 -20'`                            |
| `item-filter`                | List of Text/ITEM_KEYS   | `null`        | A list of all the item keys to ignore when managing the reward pool. If a player dies/quits from a catacomb run all items from their inventory will be added to the prize pool except for the items in this list                                                |
