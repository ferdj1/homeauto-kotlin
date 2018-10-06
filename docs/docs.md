# Database: homeauto-db

## Devices (DB)
Holds all the devices that have been connected to the system.
Maps device ID to the device information. (id -> deviceDescription)

|Attribute|Description|
|---|---|
|id|Device ID|


## Device description (DB)
This will be read from the file that manufacturer defines in device's memory.

|Attribute|Description|
|---|---|
|id|Local id of the device. SmartHome will create its own id different from this one|
|name|Name of the device. It will be used in UI to identify the device|
|manufacturer|Manufacturer of the device. Used in UI to identify the device more precisely|
|type|Type of the device(TV, AC, Lights, etc.)|
|commands|Array that holds information about commands(See: Command)|

## Command (DB)
Used to describe command that will be executed.
Device description has array 'commands' that holds commands defined here.

|Attribute|Description|
|---|---|
|id|Specifies exact command. Used in device's program to execute specific function|
|name|Used to display user-friendly name of the command|
|description|Used to describe more precisely what command does|
|parameterTypes|Array of strings that hold the names of the types(e.g. "string" for textual values)|
|returnType|Defines type of the value that the function returns(See: Supported variable types)|

## ExecutedCommand
JSON that holds information about the command being executed.
Note that there is no need for parameter types. HomeAuto will read types from the database stored on the system.
 

|Attributes|Description|
|---|---|
|deviceId|ID of the device that executed the command|
|commandId|ID of the command that will be executed on the device|
|parameters|Parameters that are passed in|

## Scenes (DB)
Scene holds information about the commands that should be executed together.
Example: Turn on the lights, Play music, Turn on the coffee machine.

|Attribute|Description|
|---|---|
|id|Unique ID of the scene|
|name|Name that has been set by the user to easily identify their scene|
|commands|Array of commands that should be executed(See: ExecutedCommand)|

- There may be support for delayed commands.

## Rooms (DB)
Room represents physical room where many devices are set up.
User can add different devices to a room where he can have an organized view.

|Attribute|Description|
|---|---|
|id|Unique ID of the room|
|name|Room's name that the user has defined|
|type|Type of the room(kitchen, living room, bathroom, etc.)|

## Subscriptions (DB)
Each device can listen to commands executed by the other device and based on their output execute their own commands.
User defines which device and command is observed and which device and command will be executed.

|Attribute|Description|
|---|---|
|observedDeviceId|ID of the device that is observed|
|observedCommandId|ID of the command that is observed|
|observerList|Array that holds observers|

### Observer (~DB, Subscriptions)
Not a collection in the database.
JSON object with information about the observer.

|Attribute|Description|
|---|---|
|observerDeviceId|ID of the observer device|
|executedCommandId|ID of the command that will be executed|
|useOption|Boolean value that says whether comparison should be done|
|optionType|Holds the type of the comparison(numbers can be compared in many ways, string can be done in one, and booleans can either be true or false)|
|sign|Comparison sign(LT, GT, EQ, etc.)|
|comparedValue|Value that should be compared with|

## ScheduledCommands (DB)
Commands can be scheduled to execute at certain interval.

|Attribute|Description|
|---|---|
|deviceId|Device ID|
|commandId|Command ID|
|parameters|Parameters being passed in|
|interval|Interval|


## Supported variable types
HomeAuto supports basic JSON/JavaScript types:

|Type|Textual representation|Similar types|
|---|---|---|
|String|"string"|String(Java, C#, Kotlin, etc.), char*(C)|
|Number|"number"|Double(Java, C#, Kotlin, etc.)|
|Boolean|"boolean"|Bool/Boolean|

- There may be support for more specific primitives later(int, short, byte, etc.).


# WebSocket Information
**TODO**