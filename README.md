# MinimumPlayers Minecraft Plugin

A simple Minecraft plugin that restricts player movement until a specified number of players are online.

## Features

- Configurable minimum number of players required to allow movement.
- Notifies all players about the current online count and whether the server is locked or unlocked upon a player joining or leaving.

## Setup

1. Place `minimumplayers-0.x.x.jar` in your server's `plugins` folder.
2. Start your server. This will generate a default `config.yml` for the plugin.
3. If desired, modify `config.yml` to set a custom minimum player count.

## Configuration

`config.yml` parameters:

- `minimumNumberOfPlayers`: The minimum number of players required for movement to be unlocked. Default value is 3.

## Contributing

Contributions, issues, and feature requests are welcome!

## License

This project is distributed under the MIT license.