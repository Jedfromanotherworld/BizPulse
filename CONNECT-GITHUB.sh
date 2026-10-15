#!/bin/sh
set -eu
cd "$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)"
command -v git >/dev/null 2>&1 || { echo "Install Git first."; exit 1; }
if [ ! -e .git ]; then git init -b main; fi
bizpulse_url='https://github.com/Jedfromanotherworld/BizPulse.git'
bizpulse_origin=$(git remote get-url origin 2>/dev/null || true)
if [ -z "$bizpulse_origin" ]; then
    git remote add origin "$bizpulse_url"
elif [ "$bizpulse_origin" != "$bizpulse_url" ]; then
    echo "A different origin is configured. No changes made to it."
    git remote -v
    exit 1
fi
git remote -v
echo "Local connection ready. Follow START-HERE-GITHUB.md to commit and push."
