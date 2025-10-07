# Chips Manager

**Chips Manager** is a lightweight Android app that helps groups of players manage poker chips during real-life card sessions. The app focuses solely on chip management — it does **not** implement any card game rules or round enforcement. Instead, it offers a simple, trust-based interface to track bets, pots, and player chip counts in real time across multiple devices.

---

## Key Features

- **Firebase Authentication**: Users sign in with a username and password powered by Firebase Auth.
- **Realtime Sync**: All changes (bets, pot updates, joins/exits) are synchronized across devices using Firebase Realtime Database.
- **Multiple Lobbies**: Create or join lobbies that have different initial chip budgets.
- **Player Actions**:
  - **Bet** — place an amount into the pot.
  - **Take All / Take Partial** — remove either the full pot or a chosen amount from the pot into the player’s chip count.
  - **Fold** — skip betting while the current pot is active; when the pot becomes empty, that player may bet again.
  - **Exit** — leave the lobby.
- **No enforced game logic**: Actions can be performed at any time; there is no automated round progression or rule enforcement. This design makes the app flexible for casual, in-person play.

---

## Why use Chips Manager

Chips Manager is ideal when you have a deck of cards but no physical chips. Players keep phones at the table, sync through Firebase, and manage chips digitally while deciding rounds and fairness in person.

---

## Limitations & Design Notes

- **Trust model**: The app assumes players manage fairness and rule disputes in person. There is no anti-cheat or authoritative round enforcement.
- **No round mechanism**: Players can act at any time. If you need strict turn-taking or round rules, consider adding a server-side coordinator or stronger database rules and state transitions.
- **Authentication**: Currently relies on Firebase Auth; username/password management should be treated according to best security practices.

---

## Development Notes

- **Language**: Java
- **Platform**: Android (open with Android Studio)
- **Backend**: Firebase Realtime Database & Firebase Authentication

---

## License

This project is released under the **MIT License**. See the `LICENSE` file for details.

---

*Chips Manager — manage chips, not rules.*

