# Design an in-memory file system


- Support files and directories
- Directory can contain: Files and Sub-directories
- Operations:
  - mkdir(path)
  - ls(path)
  - createFile(path, content)
  - readFile(path)
  - appendToFile(path, content)
  - delete(path)
- Paths are Unix-style: /a/b/c
- ls():
  - If directory → list contents
  - If file → return file name
- In-memory (no persistence required)
- Optimized for: Fast lookup (path traversal)
- Assume: Single-threaded (unless discussed later)