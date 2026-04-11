const CFG = {
  SIZE: 6,
  N_WUMPUS: 2,
  N_PITS: 4,
  N_GOLD: 3,
  N_BATS: 2,
  N_ARROWS: 1,
  COST_ACTION: -1,
  COST_SHOOT: -10,
  REW_GOLD: 1000,
  PEN_PIT: -1000,
  PEN_WUMPUS: -1000,
};

const DIRS = [
  { name: "Leste", icon: "→", dr: 0, dc: 1 },
  { name: "Norte", icon: "↑", dr: -1, dc: 0 },
  { name: "Oeste", icon: "←", dr: 0, dc: -1 },
  { name: "Sul", icon: "↓", dr: 1, dc: 0 },
];

const key = (r, c) => r + "," + c;
