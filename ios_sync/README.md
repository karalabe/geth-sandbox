## Geth sync on iOS

This is a simple project that syncs with the Ethereum network using an in-process
instance of the Go Ethereum implementation on iOS.

To run it you must download a copy of the `Geth.framework` Xcode bundle from either the
[experimental nightly builds](https://bintray.com/karalabe/ethereum/geth-develop/view),
the [official releases](https://github.com/ethereum/go-ethereum/wiki/Building-Ethereum)
(if support is extended to it) or build one from the sources yourself. The project is
preconfgured to look for said framework bundle in the same folder this `readme` resides
in.
