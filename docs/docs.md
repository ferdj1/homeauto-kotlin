# Database: homeauto-db

## Devices (DB)
Holds all the devices that have been connected to the system.
Maps device ID to the device information. (id -> deviceDescription)

**Currently redundant, will be used when new ID system is implemented**

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
|getSetType|Used to represent whether command will be used to mutate data in device or will it be used to get data from the device(See: Get-Set Types)|
|displayText|If 'getSetType' is GET, there needs to be a text that will be displayed next to the received data. This is null if there is no GET type|
|parameterDescriptions|Array of ParameterDescription objects|
|returnType|Defines type of the value that the function returns(See: Supported variable types)|

## ParameterDescription (~DB, Command)
Used to define limits of what can be passed into the function that command defined.
This object is useful when system is creating control panel UI for the device.
Note on min, max and values: Values will be passed in as strings and based on parameter type, casted into the appropriate type.

|Attribute|Description|
|---|---|
|name|Name that describes what parameter does in function(e.g.: If there needs to be a gradient of colors selected, there should be a label for first and second color|
|type|Defines type of the parameter(See: Supported variable types)|
|specialType|Defines meaning of the value(e.g. color)|
|limitType|Defines whether values being passed into the function can be from a certain set of values, or from a minimum to maximum value, or whatever they want to be(See: Parameter Limit Types)|
|min|Defines minimum value in string. If there is no minimum value, it must be set to null.| 
|max|Defines maximum value in string. If there is no maximum value, it must be set to null.|
|values|Array that holds every allowed value that can be passed into the function| 
|defaultValue|Default value for this parameter|

### Parameter Limit Types
These define what are the limits to the values being passed in to the function.

|Attribute|Description|
|---|---|
|UNDEFINED|Value cannot be placed in other three categories|
|NO_LIMIT|Value doesn't have limit|
|LIMITED_SET|Value must be from a set of values defined in ParameterDescription's values array|
|MIN_MAX|Value must be between min and max defined in ParameterDescription|

### Get-Set Types
Data can be received from the device and displayed on the system's UI. Other thing that can be done is that data can be sent to device.

|Attribute|Description|
|---|---|
|GET|Used to receive data from the device|
|SET|Used to send data to device|

## ExecutableCommand
JSON that holds information about the command being executed.
Note that there is no need for parameter types. HomeAuto will read types from the database stored on the system.

|Attributes|Description|
|---|---|
|deviceId|ID of the device that will execute command|
|commandId|ID of the command that will be executed on the device|
|parameters|Parameters that are passed in|

## ExecutedCommand
JSON that holds information about the command that was executed and result of it.

|Attributes|Description|
|---|---|
|deviceId|ID of the device that executed command|
|commandId|ID of the command that was executed|
|parameters|Array of arguments that were given to the command|
|result|Result of the execution|



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
|devices|List of devices in the room|

## Subscriptions (DB)
Each device can listen to commands executed by the other device and based on their output execute their own commands.
User defines which device and command is observed and which device and command will be executed.

|Attribute|Description|
|---|---|
|id|Unique ID of the subscription|
|observedDeviceId|ID of the device that is observed|
|observedCommandId|ID of the command that is observed|
|observerList|Array that holds observers|

### Observer (~DB, Subscriptions)
Not a collection in the database.
JSON object with information about the observer.

|Attribute|Description|
|---|---|
|observerDeviceId|ID of the observer device|
|observerCommandId|ID of the command that will be executed|
|parameters|Parameters that will be given to the command during execution (See: Parameter)|
|parameterComparators|User-defined checks for observed command (See: ParameterComparator)|

### Parameter(~DB, Subscriptions)
|Attribute|Description|
|---|---|
|usesObservedValue|Boolean value that says if some value from observed command should be used|
|value|String value that will be parsed into the needed type. This value is defined by the user, if usesObservedValue if false, this value is passed to the command|
|observedParameterIndex|If usesObservedValue is true, parameter from observed command at specified index will be used instead of previously defined 'value'|

### ParameterComparator(~DB, Subscriptions)
|Attribute|Description|
|---|---|
|useOption|Boolean value that says whether comparison should be done|
|optionType|Holds the type of the comparison(numbers can be compared in many ways, string can be done in one, and booleans can either be true or false)|
|sign|Comparison sign(LT, GT, EQ, etc.)|
|comparedValue|Value that should be compared with|

## ScheduledCommands (DB)
Commands can be scheduled to execute at certain interval.

|Attribute|Description|
|---|---|
|id|Unique ID of the scheduled command|
|deviceId|Device ID|
|commandId|Command ID|
|parameters|Parameters being passed in|
|interval|Interval|
|intervalMetric|Metric used for interval(sec, min, hour)|


## Supported variable types
HomeAuto supports basic JSON/JavaScript types:

|Type|Textual representation|Similar types|Description|
|---|---|---|---|
|String|"string"|String(Java, C#, Kotlin, etc.), char*(C)|-|
|Number|"number"|Double(Java, C#, Kotlin, etc.)|See: JavaScript's number format|
|Integer|"integer"|Integer(Java, C#, Kotlin, etc.) |-|
|Boolean|"boolean"|Bool/Boolean|-|
|Null|"null"|null, NULL, 0|Used to represent unused value|


# WebSocket Information
WebSocket communication will be based on few different message types.
There are types where device sends its description, where it executes a command, etc.

## From the server's point of view
These types define messages sent TO a server. Server acts based on the message's type

|Message Type|Description|
|---|---|
|description|Device sends its description. This will usually be done after connection has been established|
|clientToServerExecutedCommand|Device signals that a command has been executed. Any device that is interested in that will be notified by the server|

## From the client's point of view
These types define messages sent FROM a server to a client.
Manufacturer needs to define what will happen internally on the device's system.
There are few message types and actions that should happen when client receives them.
Rules are loosely defined through a description, there is no interface or anything similar that defines exactly what should happen.
This allows the client to write its program in any language that supports WebSockets and that can execute functions based on reflection.

|Message Type|Description|
|---|---|
|executeCommand|Device needs to execute a command that is defined in this message. After that, it needs to send 'clientToServerExecutedCommand' to notify the server and other devices that it has executed a command internally|
|serverToClientExecutedCommand|When some device has executed a command, server will notify every subscribed device using this message|


# Other
## Bootstrapping

Task: find a way for the device to easily connect to server's network and server itself.
 
