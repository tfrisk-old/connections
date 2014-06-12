# connections

Visualizing connections between politicians and organizations.

## Usage

First you have to have a neo4j server running somewhere.
Then you have to change the server configuration to match your setup (src/sidonnaisuudet/neo4j.clj).

Project uses clojurescript for fancier UI. To compile them to javascript use
<code>lein cljsbuild once</code>.

To start the included web server use <code>lein ring server</code>.

## License

Copyright © 2014 Teemu Frisk

Distributed under the Eclipse Public License.
