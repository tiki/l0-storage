/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

module.exports = async () => {
  return {
    verbose: true,
    globals: {
      crypto: require('crypto')
    }
  }
}
