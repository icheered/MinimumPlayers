# MinimumPlayers Minecraft Plugin

A simple Minecraft plugin that restricts player movement until a specified number of players are online.

## Features

- Configurable minimum number of players required to allow movement.
- Notifies all players about the current online count and whether the server is locked or unlocked upon a player joining or leaving.

## Setup

1. Place `minimumplayers-0.x.x.jar` in your server's `plugins` folder.
2. Start your server. This will generate a default `config.yml` for the plugin.
3. If desired, modify `config.yml` to change things like the minimum player count.

## Configuration

`config.yml` parameters:

- `minimumNumberOfPlayers`: The minimum number of players required for movement to be unlocked. Default value is 3.
- `allowGracePeriod`: Whether to allow a grace period (i.e. allow players to move for X minutes after the playercount dropped below minimum).
- `gracePeriodMinutes`: The number of minutes to allow movement after the playercount dropped below minimum. Default value is 5 minutes.
- `sendMessageWhenFrozen`: Whether to send a message when a player tries to move while the server is frozen.
- `frozenMessageIntervalSeconds`: The number of seconds to wait before sending another message to a player who is trying to move while the server is frozen. Default value is 5 seconds. Increase the number if you don't want to spam players.

## Contributing

Contributions, issues, and feature requests are welcome!

## License

This project is distributed under the MIT license.

## Change Log
V1.0.0: Plugin released.
V1.1.0: Added grace period feature, and made everything configurable.