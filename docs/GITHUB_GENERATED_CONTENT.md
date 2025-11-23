# GitHub Auto-Generated Content in Changelog System

## Overview

The changelog system now combines **manual changelogs** with **GitHub-generated content** to give the complete picture:

1. **Manual Changelog** - User-focused, curated changes (what you write in `changelogs/feat-*.md`)
2. **GitHub-Generated Commits** - Complete technical detail (every commit with hash)

## In Pull Requests (Promotion Workflows)

### What's Included

Each promotion PR now includes a collapsible "Commits" section:

```markdown
## Automated Promotion to `dev`

**Feature:** `feature/user-auth`
**Tag:** `feat-user-auth-dev`

---

## 📋 Changelog

### Added
- OAuth2 authentication with Google provider
- User profile management

---

<details>
<summary>📝 Commits in this promotion</summary>

- feat: add OAuth2 Google provider (a1b2c3d)
- feat: implement user profile page (e4f5g6h)
- fix: resolve token expiration issue (i7j8k9l)
- test: add integration tests for auth (m0n1o2p)
- docs: update API documentation (q3r4s5t)

</details>

---

<sub>This PR was automatically created by the promote-to-dev workflow.</sub>
```

### How It Works

The workflow uses `git log` to compare branches:

```bash
# Get commits between target branch and feature branch
git log origin/dev..feature/user-auth --oneline --pretty=format:"- %s (%h)"
```

**Output example:**
```
- feat: add OAuth2 Google provider (a1b2c3d)
- feat: implement user profile page (e4f5g6h)
- fix: resolve token expiration issue (i7j8k9l)
```

### Why Collapsible?

The `<details>` tag keeps PRs clean:
- **Collapsed by default** - Manual changelog is primary focus
- **Expandable** - Reviewers can see full commit history if needed
- **Complete audit trail** - Nothing is hidden

## In GitHub Releases

### Pre-releases (v*-rc*)

The `changelog-aggregate-prerelease.yml` workflow creates pre-releases with:

```yaml
- name: Create GitHub Pre-release
  uses: softprops/action-gh-release@v2
  with:
    prerelease: true
    body_path: ${{ steps.extract.outputs.body_file }}  # Manual changelog
    generate_release_notes: true                        # GitHub auto-generation
    append_body: true                                   # Combines both!
```

**Result:**
```markdown
## v1.1.0-rc

### Added
- OAuth2 authentication with Google provider *(feat-user-auth)*
- Project templates *(feat-templates)*

### Changed
- API now requires authentication *(feat-user-auth)*

---

## What's Changed
* feat: add OAuth2 Google provider by @AndriFannar in #123
* feat: implement user profile page by @AndriFannar in #124
* feat: add project templates by @AndriFannar in #125
* fix: resolve token expiration issue by @AndriFannar in #126

**Full Changelog**: https://github.com/owner/repo/compare/v1.0.0...v1.1.0-rc
```

### Production Releases (v*)

Same structure in `changelog-finalize-release.yml`:

```yaml
- name: Create GitHub Release
  uses: softprops/action-gh-release@v2
  with:
    body_path: ${{ steps.extract.outputs.body_file }}  # Manual changelog
    generate_release_notes: true                        # GitHub auto-generation
    append_body: true                                   # Combines both!
```

## Comparison: Manual vs Generated

### Manual Changelog (What You Write)
✅ User-focused  
✅ High-level features  
✅ Breaking changes highlighted  
✅ Migration guidance  
✅ Categorized (Added/Changed/Fixed)  

**Example:**
```markdown
### Added
- OAuth2 authentication with Google provider
- User can now export reports as PDF

### Breaking Changes
- `/api/users` endpoint now requires authentication
- Migration: Update client to include JWT token in headers
```

### GitHub-Generated Content
✅ Every commit listed  
✅ PR links  
✅ Contributor credits  
✅ Compare link  
✅ Complete audit trail  

**Example:**
```markdown
## What's Changed
* feat: add OAuth2 Google provider by @AndriFannar in #123
* feat: implement user profile page by @AndriFannar in #124
* feat: add PDF export functionality by @AndriFannar in #125
* fix: resolve token expiration issue by @AndriFannar in #126
* test: add integration tests for auth by @AndriFannar in #127
* docs: update API documentation by @AndriFannar in #128
* chore: update dependencies by @AndriFannar in #129

**Full Changelog**: https://github.com/owner/repo/compare/v1.0.0...v1.1.0
```

## Best of Both Worlds

| Audience | Reads |
|----------|-------|
| **End users** | Manual changelog (clear, focused) |
| **Developers** | Both (context + technical detail) |
| **Auditors** | Generated content (complete history) |
| **Contributors** | Generated content (credit for work) |

## Structure Summary

### Promotion PRs
```
[Manual Changelog - prominent]
  ↓
<details> [Commits - collapsible] </details>
```

### Releases
```
[Manual Changelog - first]
  ↓
[Horizontal rule]
  ↓
[GitHub Generated - second]
```

## Configuration

All three promotion workflows include commits:
- ✅ `promote-to-dev.yml`
- ✅ `promote-to-test.yml`
- ✅ `promote-to-preprod.yml`

Both release workflows use `generate_release_notes: true`:
- ✅ `changelog-aggregate-prerelease.yml`
- ✅ `changelog-finalize-release.yml`

## Example Flow

1. **Feature Development**
   ```
   Commits:
   - feat: add OAuth2
   - feat: add profiles
   - fix: token bug
   - test: add tests
   - docs: update docs
   ```

2. **Manual Changelog** (`changelogs/feat-user-auth.md`)
   ```markdown
   ### Added
   - OAuth2 authentication
   - User profiles
   
   ### Fixed
   - Token expiration issue
   ```

3. **Promotion PR to dev**
   ```markdown
   ## 📋 Changelog
   [manual content above]
   
   <details>
   <summary>📝 Commits in this promotion</summary>
   - feat: add OAuth2 (a1b2c3d)
   - feat: add profiles (e4f5g6h)
   - fix: token bug (i7j8k9l)
   - test: add tests (m0n1o2p)
   - docs: update docs (q3r4s5t)
   </details>
   ```

4. **Pre-release (v1.1.0-rc)**
   ```markdown
   [Aggregated manual changelogs]
   
   ---
   
   ## What's Changed
   * feat: add OAuth2 by @user in #123
   * feat: add profiles by @user in #124
   ...
   
   **Full Changelog**: compare/v1.0.0...v1.1.0-rc
   ```

## Benefits

✅ **Transparency** - Nothing is hidden, full history available  
✅ **Context** - Manual changelog explains "why", commits show "what"  
✅ **Credit** - Contributors get recognition in generated section  
✅ **Audit Trail** - Complete lineage from feature to production  
✅ **Professional** - Industry-standard approach used by major projects  

## Customization

### Show More Commit Details

In promotion workflows, you can add more info:

```bash
# Add author and date
git log origin/dev..${BRANCH} --pretty=format:"- %s (%h) - %an, %ar"

# Output:
# - feat: add OAuth2 (a1b2c3d) - Andri Fannar, 2 days ago
```

### Group Commits by Type

```bash
# Separate feat/fix/chore
echo "#### Features" >> "$PR_BODY_FILE"
git log origin/dev..${BRANCH} --grep="^feat:" --pretty=format:"- %s (%h)" >> "$PR_BODY_FILE"

echo "#### Bug Fixes" >> "$PR_BODY_FILE"
git log origin/dev..${BRANCH} --grep="^fix:" --pretty=format:"- %s (%h)" >> "$PR_BODY_FILE"
```

### Exclude Commits

```bash
# Skip certain commit types
git log origin/dev..${BRANCH} \
  --grep="^docs:" --grep="^chore:" --invert-grep \
  --pretty=format:"- %s (%h)"
```

## Files Updated

- [promote-to-dev.yml](./promote-to-dev.yml) - Added commits section
- [promote-to-test.yml](./promote-to-test.yml) - Added commits section
- [promote-to-preprod.yml](./promote-to-preprod.yml) - Added commits section
- [changelog-aggregate-prerelease.yml](./changelog-aggregate-prerelease.yml) - Already has `generate_release_notes: true`
- [changelog-finalize-release.yml](./changelog-finalize-release.yml) - Already has `generate_release_notes: true`

Copy these files to your `.github/workflows/` and enjoy the combined changelog power! 🚀
