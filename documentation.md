```mermaid
sequenceDiagram
    participant user
    participant rest
    participant domain
    participant database
    
    user->>rest: Rest-Endpoint "Create new Game"
    rest-->domain: Request "Create new Game"
    domain-->database: Query
```