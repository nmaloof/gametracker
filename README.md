# Gametracker

## Introduction

The purpose of this application is to provide an HTTP backend for tracking and monitoring games played amongst others. Additionally, a simple frontend is also provided.

> **Note**: The project **does not** scrrape/connect to online API's to manually fetch the data.

## Development

In order to start working, make sure the following are installed: `java`, `sbt`, `nodejs`, and `podman`. Additionally, `just` may be installed to use the helper recipes provided.

Before running any of the backend/frontend code, please run `podman play kube devops/pod.yml` to spin up the containers running the necessary services (e.g: postgres, redis).

- Starting the development server with `sbt backend/reStart`
- Stopping the development server with `sbt backend/reStop`
- Transpiling the frontend code with `sbt frontend/fastOptJs`

### Tour

The project is structured as a multi-project sbt build with the following structure:

- **[Backend](modules/backend/)**
    - Scala 3
    - `http4s` and `cats-effect`
    - Notes:
        - [Algebras](modules/backend/src/main/scala/gametracker/algebras/) define traits with the behavior used by _services_ and _repositories_.
- **[Frontend](modules/frontend/)**
    - Scala 3
    - `scalajs` and `laminar`
- **[Shared](modules/shared/)**
    - Scala 3

