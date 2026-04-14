package com.gamelauncher;

import com.gamelauncher.model.Game;
import com.gamelauncher.model.GameHyperlink;
import com.gamelauncher.model.GameLibrary;
import com.gamelauncher.model.PCGame;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Test class for the Tony Game Launcher - Java Edition application.
 * Tests all model classes and their functionality.
 */
public class GameLauncherTest {
    
    private static int testsPassed = 0;
    private static int testsFailed = 0;
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  Tony Game Launcher - Java Edition - Test Suite");
        System.out.println("========================================\n");
        
        // Run all tests
        testPCGameCreation();
        testPCGameValidation();
        testPCGameLaunchValidation();
        testGameLibraryCreation();
        testGameLibraryAddRemove();
        testGameLibrarySearch();
        testGameLibraryPersistence();
        testGameHyperlinkCreation();
        testGameHyperlinkValidation();
        testCategoryPersistenceAfterRemoval();
        testInheritanceAndPolymorphism();
        
        // Print summary
        System.out.println("\n========================================");
        System.out.println("  Test Summary");
        System.out.println("========================================");
        System.out.println("Tests Passed: " + testsPassed);
        System.out.println("Tests Failed: " + testsFailed);
        System.out.println("Total Tests:  " + (testsPassed + testsFailed));
        
        if (testsFailed == 0) {
            System.out.println("\n✓ All tests passed!");
        } else {
            System.out.println("\n✗ Some tests failed!");
            System.exit(1);
        }
    }
    
    private static void testPCGameCreation() {
        System.out.println("Test: PCGame Creation");
        try {
            PCGame game = new PCGame(
                "game001",
                "Test Game",
                "C:/Games/TestGame/game.exe",
                "A test game for validation",
                "C:/Games/TestGame",
                "Steam"
            );
            
            assert game.getGameId().equals("game001") : "Game ID mismatch";
            assert game.getTitle().equals("Test Game") : "Title mismatch";
            assert game.getExecutablePath().equals("C:/Games/TestGame/game.exe") : "Path mismatch";
            assert game.getPlatform().equals("Steam") : "Platform mismatch";
            assert game.getPlaytimeHours() == 0.0 : "Initial playtime should be 0";
            
            System.out.println("  ✓ PCGame creation successful");
            System.out.println("    - " + game);
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  ✗ PCGame creation failed: " + e.getMessage());
            testsFailed++;
        }
        System.out.println();
    }
    
    private static void testPCGameValidation() {
        System.out.println("Test: PCGame Validation");
        try {
            PCGame game = new PCGame();
            
            // Test valid game ID
            game.setGameId("valid-id");
            assert game.getGameId().equals("valid-id");
            
            // Test invalid game ID
            try {
                game.setGameId("");
                System.out.println("  ✗ Empty game ID should throw exception");
                testsFailed++;
                return;
            } catch (IllegalArgumentException e) {
                System.out.println("  ✓ Empty game ID correctly rejected");
            }
            
            // Test valid title
            game.setTitle("Valid Title");
            assert game.getTitle().equals("Valid Title");
            
            // Test invalid title
            try {
                game.setTitle(null);
                System.out.println("  ✗ Null title should throw exception");
                testsFailed++;
                return;
            } catch (IllegalArgumentException e) {
                System.out.println("  ✓ Null title correctly rejected");
            }
            
            System.out.println("  ✓ PCGame validation successful");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  ✗ PCGame validation failed: " + e.getMessage());
            testsFailed++;
        }
        System.out.println();
    }
    
    private static void testPCGameLaunchValidation() {
        System.out.println("Test: PCGame Launch Validation");
        try {
            // Create a game with non-existent executable
            PCGame invalidGame = new PCGame(
                "invalid001",
                "Invalid Game",
                "C:/NonExistent/Game.exe",
                "This game doesn't exist"
            );
            
            // Should return false for invalid executable
            boolean isValid = invalidGame.isValid();
            assert !isValid : "Non-existent executable should be invalid";
            System.out.println("  ✓ Non-existent executable correctly identified as invalid");
            
            // Create a temporary file to test valid case
            File tempFile = File.createTempFile("testgame", ".exe");
            PCGame validGame = new PCGame(
                "valid001",
                "Valid Game",
                tempFile.getAbsolutePath(),
                "This is a test executable"
            );
            
            isValid = validGame.isValid();
            assert isValid : "Existing file should be valid";
            System.out.println("  ✓ Existing executable correctly identified as valid");
            
            // Clean up
            tempFile.delete();
            
            System.out.println("  ✓ PCGame launch validation successful");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  ✗ PCGame launch validation failed: " + e.getMessage());
            testsFailed++;
        }
        System.out.println();
    }
    
    private static void testGameLibraryCreation() {
        System.out.println("Test: GameLibrary Creation");
        try {
            GameLibrary library = new GameLibrary("test_library.dat");
            
            assert library.getGameCount() == 0 : "New library should be empty";
            assert library.getCategories() != null : "Categories should not be null";
            assert library.getCategories().contains("All Games") : "Should have 'All Games' category";
            assert library.getCategories().contains("Favorites") : "Should have 'Favorites' category";
            assert library.getCategories().contains("Recently Played") : "Should have 'Recently Played' category";
            
            System.out.println("  ✓ GameLibrary creation successful");
            System.out.println("    - Categories: " + library.getCategories());
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  ✗ GameLibrary creation failed: " + e.getMessage());
            testsFailed++;
        }
        System.out.println();
    }
    
    private static void testGameLibraryAddRemove() {
        System.out.println("Test: GameLibrary Add/Remove");
        try {
            GameLibrary library = new GameLibrary("test_library.dat");
            
            PCGame game1 = new PCGame("add001", "Game One", "C:/Games/Game1.exe", "First game");
            PCGame game2 = new PCGame("add002", "Game Two", "C:/Games/Game2.exe", "Second game");
            
            // Test add
            boolean added1 = library.addGame(game1);
            assert added1 : "First game should be added successfully";
            System.out.println("  ✓ First game added successfully");
            
            boolean added2 = library.addGame(game2);
            assert added2 : "Second game should be added successfully";
            System.out.println("  ✓ Second game added successfully");
            
            assert library.getGameCount() == 2 : "Library should have 2 games";
            
            // Test duplicate add
            boolean addedDuplicate = library.addGame(game1);
            assert !addedDuplicate : "Duplicate game should not be added";
            System.out.println("  ✓ Duplicate game correctly rejected");
            
            // Test remove
            boolean removed = library.removeGame("add001");
            assert removed : "Game should be removed successfully";
            assert library.getGameCount() == 1 : "Library should have 1 game after removal";
            System.out.println("  ✓ Game removed successfully");
            
            // Test remove non-existent
            boolean removedNonExistent = library.removeGame("nonexistent");
            assert !removedNonExistent : "Removing non-existent game should return false";
            System.out.println("  ✓ Non-existent game removal correctly handled");
            
            // Clean up
            library.clear();
            new File("test_library.dat").delete();
            
            System.out.println("  ✓ GameLibrary add/remove successful");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  ✗ GameLibrary add/remove failed: " + e.getMessage());
            testsFailed++;
        }
        System.out.println();
    }
    
    private static void testGameLibrarySearch() {
        System.out.println("Test: GameLibrary Search");
        try {
            GameLibrary library = new GameLibrary("test_library.dat");
            
            library.addGame(new PCGame("search001", "The Witcher 3", "C:/Games/Witcher3.exe", "RPG Game"));
            library.addGame(new PCGame("search002", "Witcher 2", "C:/Games/Witcher2.exe", "Older RPG"));
            library.addGame(new PCGame("search003", "Cyberpunk 2077", "C:/Games/Cyberpunk.exe", "Sci-fi RPG"));
            
            // Test search
            List<com.gamelauncher.model.Game> results = library.searchByTitle("Witcher");
            assert results.size() == 2 : "Should find 2 games with 'Witcher' in title";
            System.out.println("  ✓ Search for 'Witcher' found " + results.size() + " games");
            
            results = library.searchByTitle("Cyber");
            assert results.size() == 1 : "Should find 1 game with 'Cyber' in title";
            System.out.println("  ✓ Search for 'Cyber' found " + results.size() + " games");
            
            results = library.searchByTitle("");
            assert results.size() == 3 : "Empty search should return all games";
            System.out.println("  ✓ Empty search returned all games");
            
            // Clean up
            library.clear();
            new File("test_library.dat").delete();
            
            System.out.println("  ✓ GameLibrary search successful");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  ✗ GameLibrary search failed: " + e.getMessage());
            testsFailed++;
        }
        System.out.println();
    }
    
    private static void testGameLibraryPersistence() {
        System.out.println("Test: GameLibrary Persistence");
        try {
            String testFile = "persistence_test.dat";
            
            // Create library and add games
            GameLibrary library1 = new GameLibrary(testFile);
            library1.clear();
            
            PCGame game = new PCGame("persist001", "Persistent Game", "C:/Games/Persist.exe", 
                                     "Test persistence", "C:/Games", "Steam", 5.5);
            game.setLaunchType(PCGame.LaunchType.LINK);
            game.setPrimaryLinkId("test-link-id");
            library1.addGame(game);
            library1.addCategory("TestCategory");
            
            // Create new library instance with same file
            GameLibrary library2 = new GameLibrary(testFile);
            
            // Verify data was loaded
            assert library2.getGameCount() == 1 : "Should load 1 game from file";
            System.out.println("  ✓ Games persisted and loaded correctly");
            
            PCGame loadedGame = (PCGame) library2.findGameById("persist001");
            assert loadedGame != null : "Should find the persisted game";
            assert loadedGame.getTitle().equals("Persistent Game") : "Title should match";
            assert loadedGame.getPlaytimeHours() == 5.5 : "Playtime should match";
            assert loadedGame.getLaunchType() == PCGame.LaunchType.LINK : "Launch type should match";
            assert "test-link-id".equals(loadedGame.getPrimaryLinkId()) : "Primary link ID should match";
            System.out.println("  ✓ Game data preserved correctly");
            
            assert library2.getCategories().contains("TestCategory") : "Custom category should be persisted";
            System.out.println("  ✓ Categories persisted correctly");
            
            // Clean up
            library2.clear();
            new File(testFile).delete();
            
            System.out.println("  ✓ GameLibrary persistence successful");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  ✗ GameLibrary persistence failed: " + e.getMessage());
            e.printStackTrace();
            testsFailed++;
        }
        System.out.println();
    }

    private static void testGameLibraryUpdateGame() {
        System.out.println("Test: GameLibrary Update Game");
        try {
            String testFile = "update_test.dat";
            GameLibrary library = new GameLibrary(testFile);
            library.clear();

            PCGame game = new PCGame("upd001", "Orig", "C:/orig.exe", "Original");
            library.addGame(game);

            game.setTitle("Updated Title");
            game.setPlatform("Epic");
            game.setPlaytimeHours(12.5);

            boolean updated = library.updateGame(game);
            assert updated : "Game should be updated";

            Game loaded = library.findGameById("upd001");
            assert loaded != null : "Loaded game should not be null";
            assert loaded.getTitle().equals("Updated Title") : "Title should be updated";
            assert ((PCGame) loaded).getPlaytimeHours() == 12.5 : "Playtime should be updated";

            library.clear();
            new File(testFile).delete();

            System.out.println("  ✓ GameLibrary update game successful");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  ✗ GameLibrary update game failed: " + e.getMessage());
            testsFailed++;
        }
        System.out.println();
    }

    private static void testGameHyperlinkPersistenceAndEdit() {
        System.out.println("Test: GameHyperlink Persistence and Edit");
        try {
            String testFile = "link_test.dat";
            GameLibrary library = new GameLibrary(testFile);
            library.clear();

            PCGame game = new PCGame("link001", "Link Game", "C:/link.exe", "Test links");
            library.addGame(game);

            GameHyperlink link = new GameHyperlink("link001", "link001", "Info", "https://example.com", "Example");
            game.addLink(link);
            library.updateGame(game);

            GameLibrary library2 = new GameLibrary(testFile);
            PCGame loadedGame = (PCGame) library2.findGameById("link001");
            assert loadedGame != null : "Loaded game should not be null";
            assert loadedGame.getLinks().size() == 1 : "Game should have 1 saved link";

            GameHyperlink loadedLink = loadedGame.getLinkById("link001");
            assert loadedLink != null : "Loaded hyperlink should not be null";
            assert loadedLink.getUrl().equals("https://example.com") : "URL should match";

            loadedLink.setUrl("https://updated.com");
            loadedGame.addLink(loadedLink);
            library2.updateGame(loadedGame);

            GameLibrary library3 = new GameLibrary(testFile);
            PCGame updatedGame = (PCGame) library3.findGameById("link001");
            assert updatedGame.getLinkById("link001").getUrl().equals("https://updated.com") : "URL should be updated";

            library3.clear();
            new File(testFile).delete();

            System.out.println("  ✓ GameHyperlink persistence and edit successful");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  ✗ GameHyperlink persistence/edit failed: " + e.getMessage());
            testsFailed++;
        }
        System.out.println();
    }

    private static void testCategoryPersistenceAfterRemoval() {
        System.out.println("Test: Category Persistence After Removal");
        try {
            String testFile = "category_remove_test.dat";
            GameLibrary library = new GameLibrary(testFile);
            library.clear();

            // ensure defaults are present and then remove
            assert library.getCategories().contains("All Games");
            assert library.getCategories().contains("Favorites");
            assert library.getCategories().contains("Recently Played");

            boolean removed = library.removeCategory("All Games");
            assert removed : "All Games should be removable";

            GameLibrary loaded = new GameLibrary(testFile);
            assert !loaded.getCategories().contains("All Games") : "All Games should remain removed across sessions";

            library.clear();
            new File(testFile).delete();

            System.out.println("  ✓ Category persistence after removal successful");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  ✗ Category persistence after removal failed: " + e.getMessage());
            testsFailed++;
        }
        System.out.println();
    }

    private static void testGameHyperlinkCreation() {
        System.out.println("Test: GameHyperlink Creation");
        try {
            GameHyperlink link = new GameHyperlink(
                "link001",
                "game001",
                "Official Wiki",
                "https://wiki.example.com",
                "Official game wiki"
            );
            
            assert link.getLinkId().equals("link001") : "Link ID mismatch";
            assert link.getGameId().equals("game001") : "Game ID mismatch";
            assert link.getName().equals("Official Wiki") : "Name mismatch";
            assert link.getUrl().equals("https://wiki.example.com") : "URL mismatch";
            
            System.out.println("  ✓ GameHyperlink creation successful");
            System.out.println("    - " + link);
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  ✗ GameHyperlink creation failed: " + e.getMessage());
            testsFailed++;
        }
        System.out.println();
    }
    
    private static void testGameHyperlinkValidation() {
        System.out.println("Test: GameHyperlink Validation");
        try {
            // Valid URLs
            GameHyperlink validLink1 = new GameHyperlink("l1", "g1", "Test", "https://example.com");
            assert validLink1.isValidUrl() : "HTTPS URL should be valid";
            System.out.println("  ✓ HTTPS URL validated");
            
            GameHyperlink validLink2 = new GameHyperlink("l2", "g2", "Test", "example.com");
            assert validLink2.isValidUrl() : "URL without protocol should be valid (auto-formatted)";
            System.out.println("  ✓ URL without protocol validated");

            GameHyperlink steamLink = new GameHyperlink("l5", "g5", "Steam Launch", "steam://rungameid/12345");
            assert steamLink.isValidUrl() : "Steam protocol URL should be valid";
            System.out.println("  ✓ Steam protocol URL validated");
            
            // Invalid URLs
            GameHyperlink invalidLink = new GameHyperlink("l3", "g3", "Test", "");
            assert !invalidLink.isValidUrl() : "Empty URL should be invalid";
            System.out.println("  ✓ Empty URL correctly rejected");
            
            // Short URL
            GameHyperlink longLink = new GameHyperlink("l4", "g4", "Test", 
                "https://very-long-url-example.com/path/to/resource/page");
            String shortUrl = longLink.getShortUrl();
            assert shortUrl.endsWith("...") : "Long URL should be shortened";
            System.out.println("  ✓ URL shortening works: " + shortUrl);
            
            System.out.println("  ✓ GameHyperlink validation successful");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  ✗ GameHyperlink validation failed: " + e.getMessage());
            testsFailed++;
        }
        System.out.println();
    }
    
    private static void testInheritanceAndPolymorphism() {
        System.out.println("Test: Inheritance and Polymorphism");
        try {
            GameLibrary library = new GameLibrary("inheritance_test.dat");
            library.clear();
            
            // Create PCGame (subclass)
            PCGame pcGame = new PCGame("inherit001", "PC Game", "C:/Games/PC.exe", "Test inheritance");
            
            // Add to library using polymorphism
            library.addGame(pcGame);
            
            // Retrieve as abstract Game type
            com.gamelauncher.model.Game retrievedGame = library.findGameById("inherit001");
            assert retrievedGame != null : "Should retrieve game";
            
            // Verify it's a PCGame
            assert retrievedGame instanceof PCGame : "Retrieved game should be PCGame instance";
            System.out.println("  ✓ Polymorphism works correctly");
            
            // Verify abstract methods
            assert retrievedGame.getGameType().equals("PC Game") : "getGameType() should return 'PC Game'";
            System.out.println("  ✓ Abstract method implementation works");
            
            // Verify inherited methods
            assert retrievedGame.getTitle().equals("PC Game") : "Inherited getter should work";
            System.out.println("  ✓ Inherited methods work correctly");
            
            // Test toString override
            String str = retrievedGame.toString();
            assert str.contains("PCGame") : "toString should contain 'PCGame'";
            System.out.println("  ✓ toString() override works");
            
            // Clean up
            library.clear();
            new File("inheritance_test.dat").delete();
            
            System.out.println("  ✓ Inheritance and polymorphism test successful");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  ✗ Inheritance and polymorphism test failed: " + e.getMessage());
            testsFailed++;
        }
        System.out.println();
    }
}
