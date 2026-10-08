# Android Architecture Patterns

Each branch implements the same counter app using a different Android architecture pattern, so you can compare them side by side. The accompanying article walks through the evolution step by step:

📖 **[The Evolution of Android Architecture Patterns](docs/architecture-patterns.md)** — UI-Centric → MVC → MVP → MVVM → MVI, with code examples, diagrams, and trade-offs.

## Branches

| Branch | Pattern | Key Idea |
|--------|---------|----------|
| [`pattern/no-pattern`](../../tree/pattern/no-pattern) | UI-Centric (God Activity) | Everything in one Activity — no separation |
| [`pattern/mvc`](../../tree/pattern/mvc) | Classic MVC | Separate Controller, but holds concrete Activity reference |
| [`pattern/mvc-loader`](../../tree/pattern/mvc-loader) | MVC with Loaders | Controller survives rotation via Loader (pre-ViewModel era) |
| [`pattern/mvp`](../../tree/pattern/mvp) | MVP | Controller → Presenter, concrete ref → View interface |
| [`pattern/mvvm`](../../tree/pattern/mvvm) | MVVM | Reactive state via StateFlow, ViewModel has zero View reference |
| [`pattern/mvi-lightweight`](../../tree/pattern/mvi-lightweight) | MVI (Lightweight) | Sealed Intents + single State + pure reducer, inline in one ViewModel |
| [`pattern/mvi-redux`](../../tree/pattern/mvi-redux) | MVI (Redux-like) | Same as above, but with a reusable `BaseMviViewModel` base class |

## Architecture Evolution at a Glance

```
God Activity → MVC → MVP → MVVM → MVI
                │      │      │      │
                │      │      │      └─ Enforced unidirectional flow,
                │      │      │         single State, sealed Intents
                │      │      └─ Reactive streams (StateFlow),
                │      │         zero View reference
                │      └─ View interface decouples Presenter
                │         from concrete Activity
                └─ Separate Controller class,
                   but still coupled to Activity
```

## Building

1. Open the project in Android Studio
2. Switch to the branch you want to explore: `git checkout pattern/mvvm`
3. Sync Gradle and run the app

Requires Android Studio Ladybug (2024.2) or later and JDK 17+.

## License

This project is licensed under the MIT License — see [LICENSE](LICENSE) for details.
