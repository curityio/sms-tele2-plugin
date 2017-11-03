# Tele2 SMS Plugin #

This plugin enables the possibility to send OTP text messages through the Tele2 service.

## Config 
Name            |   Default  | Description
----------------|  --------- | -----------
`Hostname`      |            | The hostname of the service
`Port`          |            | The port of the service
`Username`      |            | The username for the provider
`Password`      |            | The password for the provider
`Context`       |            | The context where the service is running
`Http Client`   |            | The https client to use. The client can enable or disable https for instance.
`From Number`   |            | The number that the message should be sent from. Can be a number or a name. Alphanumeric, 1-11 characters. 
`Send Flash SMS`|   false    | When enabled, the service will send flash sms, which is not stored on the device.

## Build plugin

`./gradlew dist`
