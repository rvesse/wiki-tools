# Wiki Tools

A set of APIs and utilities for working with BitBucket wikis.

Currently contains the following modules:

## wiki-data-model

A simple API for representing wikis
 
## wiki-data-parser

An API providing support for populating a wiki data model from disk plus parsing and feature extraction support.

## wiki-checker

A command line utility for checking a wiki for common issues such as broken links.

### TODO

The following additional features are planned for the tools:

- Add wiki-render module(s) which can be used to render a wiki into HTML for static deployments
 - Include the ability to render versioned content
 - Use creole-parser for Creole (https://github.com/mrico/creole-parser)
 - Use Markdown4j for Markdown (https://code.google.com/p/markdown4j/)
 - Use textile4j for Textile (http://sourceforge.net/projects/textile4j/)
 - Use JRst for RestructedText (http://maven-site.nuiton.org/jrst/en/index.html)
- Support for detecting links in ReStructedText and Textile formats
- Add wiki-indexer module(s) which can be used to index a wiki for full text search with Lucene

# License

Licensed under the MIT License
