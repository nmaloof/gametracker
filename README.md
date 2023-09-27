# Gametracker


## Introduction

The purpose of this application is to:

1. Provide a learning experience
1. HTTP backend to track and monitor games
1. Javascript frontend to provide a WebUI

## Modules

- **[Backend](modules/backend/)**
    - Scala 3
    - `http4s` and `cats-effect`
- **[Frontend](modules/frontend)**
    - Scala 3
    - `scalajs` and `laminar`
- **[Shared](modules/shared)**
    - Scala 3

## Devlopment and Running

Starting the development server: `sbt backend/reStart`

Transpiling to Javascript: `sbt frontend/fastOptJS`