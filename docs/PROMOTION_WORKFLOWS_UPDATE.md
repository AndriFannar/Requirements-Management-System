# Updated Promotion Workflows - Changelog Extraction

## What Changed

Your promotion workflows (`promote-to-dev.yml`, `promote-to-test.yml`, `promote-to-preprod.yml`) have been updated to automatically extract and include the feature's changelog in the PR body.

## Changes Made

### Before
```yaml
- name: Create PR from feature to dev
  run: |
    BODY="Automated promotion of `${BRANCH}` to `dev` for tag `${TAG}`."
    gh pr create --body "$BODY"
```

### After
```yaml
- name: Extract changelog content
  id: changelog
  run: |
    # Find and read the feature's changelog file
    CHANGELOG_FILE="changelogs/feat-${FEATURE_NAME}.md"
    if [ -f "$CHANGELOG_FILE" ]; then
      # Store content in GitHub Actions output
      echo "content<<EOF" >> $GITHUB_OUTPUT
      cat "$CHANGELOG_FILE" >> $GITHUB_OUTPUT
      echo "EOF" >> $GITHUB_OUTPUT
      echo "has_changelog=true" >> $GITHUB_OUTPUT
    fi

- name: Create PR from feature to dev
  run: |
    # Build rich PR body with changelog content
    BODY="## Automated Promotion to \`dev\`
    
**Feature:** \`${BRANCH}\`
**Tag:** \`${TAG}\`

---

## 📋 Changelog

${{ steps.changelog.outputs.content }}

---

<sub>This PR was automatically created by the promote-to-dev workflow.</sub>"
    
    gh pr create --body "$BODY"
```

## Example PR Body

When you tag `feat-user-auth-dev`, the PR body will look like:

```markdown
## Automated Promotion to `dev`

**Feature:** `feature/user-auth`
**Tag:** `feat-user-auth-dev`

---

## 📋 Changelog

### Added
- OAuth2 authentication with Google and GitHub providers
- JWT token-based session management
- User profile management page with avatar upload

### Changed
- All API endpoints now require authentication
- Login page redesigned with social login buttons

### Technical Notes
- Database migration: `V003__create_users.sql`
- New environment variables: `OAUTH_CLIENT_ID`, `OAUTH_CLIENT_SECRET`
- Breaking change: `/api/users` endpoint now requires JWT token

---

<sub>This PR was automatically created by the promote-to-dev workflow.</sub>
```

## Benefits

✅ **Reviewers see what's changing** - No need to hunt for the changelog file  
✅ **Better context** - Understanding what to test is clearer  
✅ **Audit trail** - Changelog is preserved in the PR description  
✅ **Professional** - Looks polished and well-organized  
✅ **Consistent** - Same format for every promotion PR  

## How It Works

1. **Tag pushed**: `git tag feat-user-auth-dev && git push origin feat-user-auth-dev`
2. **Workflow triggers**: `promote-to-dev.yml` runs
3. **Branch identified**: Finds `feature/user-auth` from the commit
4. **Changelog found**: Reads `changelogs/feat-user-auth.md`
5. **PR created**: With full changelog in the body

## Edge Cases Handled

### No Changelog Found
If the changelog file doesn't exist, the PR body will show:
```markdown
## Automated Promotion to `dev`

**Feature:** `feature/xyz`
**Tag:** `feat-xyz-dev`

---

⚠️ No changelog found for this feature.

---

<sub>This PR was automatically created by the promote-to-dev workflow.</sub>
```

### Empty Changelog
The content will be included as-is, including the template structure if the developer hasn't filled it out yet.

### Multiple Features
Each feature gets its own PR with its own changelog. They don't interfere with each other.

## Testing

### Test the promote-to-dev workflow:
```bash
# 1. Create a feature branch with changelog
git checkout -b feature/test-changelog-extraction
# (edit changelogs/feat-test-changelog-extraction.md)

# 2. Commit and push
git add changelogs/feat-test-changelog-extraction.md
git commit -m "feat: test changelog extraction"
git push origin feature/test-changelog-extraction

# 3. Tag for dev promotion
git tag feat-test-changelog-extraction-dev
git push origin feat-test-changelog-extraction-dev

# 4. Check the PR that gets created!
```

The PR should appear with your changelog content beautifully formatted in the body.

## Files to Update

Replace these files in your `.github/workflows/` directory:
- `promote-to-dev.yml`
- `promote-to-test.yml`
- `promote-to-preprod.yml`

## Compatibility

✅ Works with existing changelog validation workflow  
✅ Works with changelog aggregation for pre-releases  
✅ No breaking changes to your existing flow  
✅ Backwards compatible (handles missing changelogs gracefully)  

## Next Steps

1. Copy the updated workflow files to your repo
2. Push to GitHub
3. Test with a feature branch and tag
4. Enjoy automatic changelog inclusion! 🎉
