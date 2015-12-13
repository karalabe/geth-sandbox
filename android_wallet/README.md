## Ethereum wallet on Android

This is a simple project that syncs with the Ethereum network using an in-process
instance of the Go Ethereum implementation on Android and starts up a wallet in a
webview container (code is a repo snapshot, no warranties , don't use it for real
funds).

To run it you must download a copy of the `geth.aar` Android archive from either the
[experimental nightly builds](https://bintray.com/karalabe/ethereum/geth-develop/view),
the [official releases](https://github.com/ethereum/go-ethereum/wiki/Building-Ethereum)
(if support is extended to it) or build one from the sources yourself. The project is
preconfgured to look for said archive in the `DroidWallet/app/libs` folder.
