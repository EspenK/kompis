package me.pckv.kompis.controller;

import me.pckv.kompis.annotation.Authorized;
import me.pckv.kompis.annotation.LoggedIn;
import me.pckv.kompis.data.Listing;
import me.pckv.kompis.data.User;
import me.pckv.kompis.service.ListingService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/listing")
public class ListingController {

    private ListingService listingService;

    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }

    /**
     * Create a new listing and set the logged in user as the owner.
     *
     * @param listing the listing to create
     * @param owner the logged in user that will be the owner of the listing
     * @return the created listing
     */
    @Authorized
    @PostMapping
    public Listing createListing(@RequestBody Listing listing, @LoggedIn User owner) {
        return listingService.createListing(listing, owner);
    }

    /**
     * Assign a user to the listing and update it.
     *
     * @param listing the listing assigned by the path variable
     * @param assignee the user to assign to the listing
     * @return the updated listing
     */
    @Authorized
    @GetMapping("/{listingId}/assign")
    public Listing assignListing(Listing listing, @LoggedIn User assignee) {
        return listingService.assignUserToListing(listing, assignee);
    }

    /**
     * Activate a listing if the logged in user is the owner of the listing.
     *
     * @param listing the listing assigned by the path variable
     * @param user the logged in user to compare with owner
     * @return the updated listing
     */
    @Authorized
    @GetMapping("/{listingId}/activate")
    public Listing activateListing(Listing listing, @LoggedIn User user) {
        return listingService.activateListing(listing, user);
    }

    /**
     * Deactivate a listing if the logged in user is the owner of the listing.
     *
     * @param listing the listing assigned by the path variable
     * @param user the logged in user to compare with owner
     * @return the updated listing
     */
    @Authorized
    @GetMapping("/{listingId}/deactivate")
    public Listing deactivateListing(Listing listing, @LoggedIn User user) {
        return listingService.deactivateListing(listing, user);
    }

    /**
     * Delete listing if the logged in user is the owner of the listing.
     *
     * @param listing the listing assigned by the path variable
     * @param user the logged in user to compare with owner
     */
    @Authorized
    @DeleteMapping("/{listingId}")
    public void deleteListing(Listing listing, @LoggedIn User user) {
        listingService.deleteListing(listing, user);
    }

    /**
     * Get a listing.
     *
     * @param listing the listing assigned by the path variable
     * @return the listing with the given ID
     */
    @Authorized
    @GetMapping("/{listingId}")
    public Listing getListing(Listing listing) {
        return listing;
    }

    /**
     * Get all listings.
     *
     * @return a list of all listings
     */
    @Authorized
    @GetMapping
    public List<Listing> getAllListings() {
        return listingService.getAllListings();
    }
}
