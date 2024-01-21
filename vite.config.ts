import { defineConfig } from "vite";
import scalaJSPlugin from "@scala-js/vite-plugin-scalajs";

export default defineConfig({
    root: 'modules/frontend/src/main/resources',
    plugins: [
        scalaJSPlugin({
            projectID: 'frontend'
        })
    ]
    // root: 'modules/frontend/src/main/resources'
    // base: 'http://sample.com/',
    // root: 'modules/frontend/src/main/resources',
    // server: {
    //     open: 'index.html',
    //     base: "modules/frontend/target/scala-3.3.1/frontend-fastopt/",
    // },
    // publicDir: "modules/frontend/target/scala-3.3.1/frontend-fastopt/"
    // server: {
    //     watch: {
    //         usePolling: true
    //     }
    // }
})