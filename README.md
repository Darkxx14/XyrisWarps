<h1 align="center">XyrisWarps</h1>
<p align="center">
  <strong>An optimized Spigot plugin for seamless warp and spawn management in Minecraft 1.16 to 1.20.4</strong>
</p>

---

## Overview

XyrisWarps is a robust and high-performance Spigot plugin designed to streamline warp and spawn management within your Minecraft server environment. With a focus on optimizing performance and offering essential functionalities akin to EssentialsX, XyrisWarps provides an efficient warp system without the resource-intensive nature of similar plugins.

---

## Key Features

- **Effortless Warp Creation**: Create warp points effortlessly to facilitate easy navigation across your Minecraft world.
- **Secure Permissions System**: Granular control over warp functionalities with configurable permissions for different player groups.
- **Optimized Performance**: Engineered to minimize server impact while maintaining seamless gameplay experiences.
- **Spawn System**: Options to define a spawn point and enable on-join spawn for players.

---

## Commands

### Warp Management
- `/createwarp <warpname>`: Create a new warp point with a unique identifier.
- `/deletewarp <warpname>`: Remove an existing warp point from the server.
- `/warp <warpname> [playername]`: Teleport to a specific warp point. Omitting player name will teleport the executor.

### Spawn System
- `/setspawn`: Define the respawn location where players will return upon death.

---

## Permissions

Grant or restrict access to commands using detailed permissions:

- `xyriswarps.createwarp`: Allow users to create new warp points.
- `xyriswarps.deletewarp`: Permit deletion of existing warp points.
- `xyriswarps.usewarp`: Grant access to use warp points.
- `xyriswarps.setspawn`: Allow setting the spawn location.

---

## Configuration

Fine-tune plugin behavior through the `config.yml` file:

```yaml
# config.yml

# Teleport players to spawn on join (true/false)
on-join-spawn: false
