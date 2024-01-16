podman := "podman play kube devops/pod.yml"

[private]
default:
    @ just --list

[no-exit-message]
services action:
    x={{ \
        if action == "up" { "--replace" } \
        else if action == "down" { "--down" } \
        else { error("Available actions are: up,down") } \
    }} && {{ podman }} $x

enter name command="/bin/bash":
    podman exec -it {{ name }} {{ command }}