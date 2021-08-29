`Documented by: Priyanshu Shrivastav (Technical Architect)`

# [WIP] Design documentation
## Short description
KoraKaagaz is a Java based no-authentication collaborative white board
native application for Linux which runs across the Internet. It has real-time drawing and chat support
with users joining the boards through IDs, username and profile picture. It supports
multi-colored and multi-sized pens, erasers along with different shapes. The persistence
support on the central server helps to keep the drawing saved without consuming local space.

## Teams
1. Networking
2. Processing
3. UI/UX
4. Infrastructure

## Overview of the workflow:
There will be a centralized server-client setup.
Therefore, it will have a central node acting as server (which is always expected
to stay up) and possibly multiple client (user) nodes which communicate with the
central node.
The client is turned up. UI is the main thread which creates instances of processing and content, and networking module opens a
INet socket. Processing and Content Modules subscribes to the networking module using INotification handler.

1. **STEP 1: Initialize/fetch a Board**
    - On the central node we will have a daemon that will act as an
        **Coordinator server**. It stores a map which translates the board ID to
        a port where the INet socket is open and flag bit (representing whether
        the board is active or not) for the corresponding board. Also another map
        which translates the board ID to the path of the persistence files of the
        board (if exists). This will be handled by the networking team.
    - The client node can take two actions now using the UI, either create a
        new board or demand an existing board by providing a board ID. In any
        case it has to communicate with the coordinator server.
        - In the case of creating a new board, the coordinator server creates a
            board ID, finds an unused port and creates a directory (on its local
            disk) for persistence. It then updates the aforementioned maps with
            the port and flipping the active bit, and spawns
            a board server which occupies the identified port for further
            communication. Finally it sends the new board ID and the port for the
            freshly spawned board to the client.
        - In the other case, when an existing board is demanded with a board ID
            from the client, for a valid board ID (already running board
            instance), the coordinator server uses the translation map to give
            out the port for the already running board server to the client.

![KoraKaagaz](https://user-images.githubusercontent.com/34399448/93567181-e5e5fc00-f9ab-11ea-9aa8-ff31536d562b.png)

2. **STEP 2: Connect to the Board**
   - Once, the STEP 1 is done, client has a board ID, address and port of the board
       server. Using the networking module, it sends a connection request to the
       board server to which the server responds by invoking the processing module
       for creating a new user instance by storing username, profile picture,
       address and port of the client dispatces an event to notify all the existing
       connected client nodes about the new entry.
   - The board server then confirms the connection by sending out the current
       state of the board which includes but not limited to the existing created
       Board objects, chat messages and user list. Board configurations and
       state is taken care by the processing team while for chat messages,
       content team is responsible.

![KoraKaagaz](https://user-images.githubusercontent.com/34399448/92733637-76618280-f395-11ea-8b07-ffac60f19268.png)

3. **STEP 3: Drawing and chatting**
   - Now we have a board server running along with possibly multiple clients.
       Server is aware of the address and port of all the clients and vice-versa
       for communication over the network.
   - The board server and all the clients have active event listeners which
       seek for changes in the board instances with subscription to various event
       handlers.
   - Any update such as change in board state or new chat message from either of
       the connected clients is listened by the board server event handler.
       The board server does the conditional insert into the queues, so that the worker threads of processing module and content module can deque the events and process accordingly and broadcasts the event to the rest of the clients. These
       thread publish to UI listener which redraws the frame based on the
       changes. This is how colaboration is achieved.
   - All the changes of the current session stays in the main memory.

![](https://i.imgur.com/ohxuujA.png)


4. **STEP 4: Shutdown**
   - A client requests for a shutdown for the board. Board server after
       listening to the request, triggers the persistence module to store the
       board state from main memory to the disk. It then updates the translation
       map with a flag denoting currently inactive.
   - Board server terminates itself after releasing the socket resources.
   - Client node closed itself by releasing all the resources and clearing the memory.

## Some additional pointers:
1) The Infrastructure team has to come up with a test harness which allows unit testing for each module. A Diagnostic and Logging framework is expected which allows multiple log categories that can be turned on and off independently while testing and debugging.
2) UI module is expected to deliver a developers' tool option which toggles a console to display the logs neatly. Log filtering should be possible.
3) Processing module is expected to allow transformations on the drawing objects such as translation and rotation.
 It is also required to keep local persistence of drawing objects to support undo/redo options.
4) The algorithms used for operations on drawing objects should be analysed well for time and space complexities.
5) Networking team is suggested to work with TCP based INet sockets.

