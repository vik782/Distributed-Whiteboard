# Distributed Whiteboard

Need to brainstorm ideas, sketch concepts, or just doodle with friends? Enter the Distributed Whiteboard — a collaborative drawing platform I developed as part of my course (COMP90015) at the University of Melbourne.

This shared digital whiteboard allows multiple users to connect and draw simultaneously in real-time. Whether you're drawing shapes, writing text, or experimenting with colors, everyone sees updates live as they happen. Plus, you can capture a snapshot of your masterpiece by saving the whiteboard to a file.

Perfect for remote collaboration, creative sessions, or just having fun — all in one shared space.

## Demo Footage
https://github.com/user-attachments/assets/8d6d519c-6f07-443a-ba07-e3b1bc1b1cf5

## Features

In this project, there are two types of users: **Managers** and **Users**. Both can access a shared set of core features, while Managers have additional administrative privileges. 

All _users_ can:

1. Draw resizable basic shapes: Line, Circle, Oval and Rectangle
2. Input text anywhere on the whiteboard
3. Choose from at least **16 color options** for drawing shapes or text
4. Use a **chat window** to send messages to all other connected users
5. View a list of currently **active users** on the whiteboard

While _managers_ have additional capabilities:

1. Use the **File Menu** to clear the whiteboard, open an existing file, save the whiteboard (default or custom location) and close the whiteboard
2. **Kick out** a particular user from the whiteboard

## Getting Started

> The following steps are to be used to run the JAR executables. To make any edits to the code and run directly from the source, proceed to the `/src/main/java` directories.

- Clone the project
```bash
  git clone https://github.com/vik782/Distributed-Whiteboard.git
```

- Run command to start a whiteboard server (e.g., use "localhost" as server_address to run locally and any free port for server_port)
```bash
  java -jar WhiteBoardServer.jar <server_address> <server_port>
```

- Run command to create and join as a Manager in the whiteboard server
```bash
  java -jar CreateWhiteBoard.jar <server_address> <server_port> <user_name>
```

- Run command to join as a User in the whiteboard server
```bash
  java -jar JoinWhiteBoard.jar <server_address> <server_port> <user_name>
```

- Done, happy drawing!

