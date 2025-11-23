# Bash Substitution Error Fix

## The Problem

The original implementation tried to embed changelog content directly into a bash variable:

```bash
BODY="${BODY}
## 📋 Changelog

${{ steps.changelog.outputs.content }}
"
```

**Issue:** When the changelog contained shell special characters like:
- `${{ ... }}` - Bash tries to interpret this as a variable
- `$(...)` - Bash tries to interpret this as command substitution
- Backticks `` ` `` - Bash tries to interpret as command substitution

Result: `bad substitution` error

## The Solution

Changed to a **file-based approach** using `gh pr create --body-file`:

```bash
# 1. Create temporary file
PR_BODY_FILE=$(mktemp)

# 2. Write header with heredoc (no substitution)
cat > "$PR_BODY_FILE" << 'END_OF_HEADER'
## Automated Promotion to `dev`

**Feature:** `BRANCH_PLACEHOLDER`
**Tag:** `TAG_PLACEHOLDER`

---

END_OF_HEADER

# 3. Replace only what we want substituted
sed -i "s|BRANCH_PLACEHOLDER|${BRANCH}|g" "$PR_BODY_FILE"
sed -i "s|TAG_PLACEHOLDER|${TAG}|g" "$PR_BODY_FILE"

# 4. Append changelog file directly (no interpretation)
cat "${{ steps.changelog.outputs.file }}" >> "$PR_BODY_FILE"

# 5. Use file with gh CLI
gh pr create --body-file "$PR_BODY_FILE"

# 6. Clean up
rm -f "$PR_BODY_FILE"
```

## Why This Works

### 1. Heredoc with Single Quotes (`<< 'END'`)
```bash
cat > "$PR_BODY_FILE" << 'END_OF_HEADER'
Content here is literal - no substitution!
${{ stuff }} stays as-is
$(commands) stay as-is
END_OF_HEADER
```

### 2. Direct File Concatenation
```bash
cat "$CHANGELOG_FILE" >> "$PR_BODY_FILE"
```
Just copies the file bytes - no interpretation at all!

### 3. Selective Substitution with sed
```bash
sed -i "s|BRANCH_PLACEHOLDER|${BRANCH}|g" "$PR_BODY_FILE"
```
Only replaces what we explicitly tell it to.

### 4. File-Based PR Creation
```bash
gh pr create --body-file "$PR_BODY_FILE"
```
GitHub CLI reads the file and uses it verbatim.

## Changes Made to Workflows

All three promotion workflows updated:
- ✅ `promote-to-dev.yml`
- ✅ `promote-to-test.yml`
- ✅ `promote-to-preprod.yml`

### Changelog Extraction Step
**Before:**
```yaml
- name: Extract changelog content
  id: changelog
  run: |
    echo "content<<EOF" >> $GITHUB_OUTPUT
    cat "$CHANGELOG_FILE" >> $GITHUB_OUTPUT
    echo "EOF" >> $GITHUB_OUTPUT
```

**After:**
```yaml
- name: Extract changelog content
  id: changelog
  run: |
    echo "file=${CHANGELOG_FILE}" >> $GITHUB_OUTPUT
```

Just stores the **file path**, not the content!

### PR Creation Step
**Before:**
```yaml
BODY="blah blah ${{ steps.changelog.outputs.content }}"
gh pr create --body "$BODY"
```

**After:**
```yaml
PR_BODY_FILE=$(mktemp)
# ... build PR body in file ...
cat "${{ steps.changelog.outputs.file }}" >> "$PR_BODY_FILE"
gh pr create --body-file "$PR_BODY_FILE"
```

## Benefits of File-Based Approach

✅ **No escaping needed** - Special characters stay literal  
✅ **Handles any content** - Markdown, code blocks, templates, etc.  
✅ **More maintainable** - Easier to read and modify  
✅ **Safer** - No risk of command injection  
✅ **Standard practice** - Many tools use file-based input for this reason  

## Testing

The fix will work with changelogs containing:
- `${{ github.actor }}` - GitHub Actions template syntax
- `$(date +%Y-%m-%d)` - Shell command substitutions
- Backticks in code blocks
- Single/double quotes
- Special markdown characters
- Multi-line content with complex formatting

All of these now pass through cleanly without interpretation!

## Updated Files

Download these updated versions:
- [promote-to-dev.yml](./promote-to-dev.yml)
- [promote-to-test.yml](./promote-to-test.yml)
- [promote-to-preprod.yml](./promote-to-preprod.yml)

Replace your existing files and push to GitHub. The error should be resolved! 🎉
