### IOT_Interface

The project consists of three main subsystems: the Core, rest API and Dashboard which
also contains an asynchronous javascript client which accesses the API and fetches the data
at regular intervals published by independent devices running in Core on separate threads.
Core also acts as a backend for Dashboard connecting using a persistent websocket
connection. Api persists the published data on a database

Architecture:
![Screenshot](https://github.com/moonbse/IOT_interface/blob/main/ScreenShots/Architecture.svg)
