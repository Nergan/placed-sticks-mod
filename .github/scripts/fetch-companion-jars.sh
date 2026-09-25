#!/usr/bin/env bash
# Скачивает с Modrinth игровой jar Kotlin for Forge.
# Аргументы: <каталог> <версия KFF>
set -euo pipefail

OUT_DIR="${1:?destination directory}"
KFF_VERSION="${2:?kotlin-for-forge version_number}"
USER_AGENT="${MODRINTH_USER_AGENT:-Nergan/placed-sticks-mod (https://github.com/Nergan/placed-sticks-mod)}"

mkdir -p "${OUT_DIR}"

json="$(curl -fsSL -A "${USER_AGENT}" "https://api.modrinth.com/v2/project/kotlin-for-forge/version")"
url="$(echo "${json}" | jq -r --arg v "${KFF_VERSION}" '
  first(.[] | select(.version_number == $v) | .files[] | select(.primary) | .url) // empty
')"
filename="$(echo "${json}" | jq -r --arg v "${KFF_VERSION}" '
  first(.[] | select(.version_number == $v) | .files[] | select(.primary) | .filename) // empty
')"
sha512="$(echo "${json}" | jq -r --arg v "${KFF_VERSION}" '
  first(.[] | select(.version_number == $v) | .files[] | select(.primary) | .hashes.sha512) // empty
')"

if [[ -z "${url}" || -z "${filename}" || -z "${sha512}" ]]; then
  echo "No primary Modrinth file for kotlin-for-forge version ${KFF_VERSION}" >&2
  exit 1
fi

curl -fsSL -A "${USER_AGENT}" -o "${OUT_DIR}/${filename}" "${url}"
echo "${sha512}  ${OUT_DIR}/${filename}" | sha512sum -c -
