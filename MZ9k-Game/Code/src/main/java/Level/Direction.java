package Level;

/**
 * The <code>Direction</code> enum is used by the level loader to determine where it should place
 * 3 <code>SolidWall</code> for the <code>StartCell</code> and <code>EndCell</code>, if it all.
 * The parameters that use this are <code>startPointWallDir</code> and <code>endPointWallDir</code>
 * They are initialized to <code>UNSET_CONSTANT</code> and are set by the level file. If there parameters are not found,
 * the level file is malformed. If the value for these parameters are unrecognized, they are set to <code>ERROR_CONSTANT</code>.
 */
enum Direction
{
	up, down, left, right, none, UNSET_CONSTANT, ERROR_CONSTANT
}
