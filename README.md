# WiseNote

## Prerequisites

- [Python3](https://www.python.org/)
  - [pre-commit](https://pre-commit.com/)
- JDK
  - [Couiser](https://get-coursier.io/)

## Installation

1. Clone repository

   ```shell
   git clone https://github.com/pers0n4/WiseNote.git
   cd WiseNote
   ```

2. Install pre-commit

   ```shell
   pip install pre-commit

   pre-commit install
   ```

3. Install Couiser

   - Linux & maxOS

     ```shell
     curl -fLo cs https://git.io/coursier-cli-"$(uname | tr LD ld)"
     chmod +x cs
     ```

   - Windows `CMD`

     ```cmd
     bitsadmin /transfer cs-cli https://git.io/coursier-cli-windows-exe "%cd%\cs.exe"
     .\cs
     ```
