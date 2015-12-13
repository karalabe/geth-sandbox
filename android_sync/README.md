## Geth sync on Android

This is a simple project that syncs with the Ethereum network using an in-process
instance of the Go Ethereum implementation on Android.

To run it you must download a copy of the `geth.aar` Android archive from either the
[experimental nightly builds](https://bintray.com/karalabe/ethereum/geth-develop/view),
the [official releases](https://github.com/ethereum/go-ethereum/wiki/Building-Ethereum)
(if support is extended to it) or build one from the sources yourself. The project is
preconfgured to look for said archive in the `DroidGeth/app/libs` folder.

Also take note, the Android archive requires a maximum target SDK lower than Android
Marshmallow (sdk 21 max) as Go does not yet conform fully to some binary restrictions
enforced by Android Marshmallow and above.
